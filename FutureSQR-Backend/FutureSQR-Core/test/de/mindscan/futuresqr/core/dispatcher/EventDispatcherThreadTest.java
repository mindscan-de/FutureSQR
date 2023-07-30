package de.mindscan.futuresqr.core.dispatcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;

import java.lang.Thread.State;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import de.mindscan.futuresqr.core.dispatcher.impl.SimpleEventDispatcherImpl;
import de.mindscan.futuresqr.core.events.FSqrEvent;
import de.mindscan.futuresqr.core.events.FSqrThreadSigAbortRequestEvent;

public class EventDispatcherThreadTest {

    @Test
    public void testGetName_EventDispatcherThreadCtor_expectThreadNameContainsEventDispatcher() throws Exception {
        // arrange
        EventDispatcherThread eventDispacherThread = new EventDispatcherThread( new SimpleEventDispatcherImpl() );

        // act
        String result = eventDispacherThread.getName();

        // assert
        assertThat( result, containsString( "EventDispatcher" ) );
    }

    @Test
    public void testRun_EventDispatcherStart_expectThreadIsRunnable() throws Exception {
        // arrange
        EventDispatcherThread eventDispacherThread = new EventDispatcherThread( new SimpleEventDispatcherImpl() );

        eventDispacherThread.start();
        waitForThread( eventDispacherThread );

        // act
        State result = eventDispacherThread.getState();

        eventDispacherThread.join( 500 );

        // assert
        assertThat( result, equalTo( State.RUNNABLE ) );
    }

    @Test
    public void testRun_EventDispatcherStartDispatchSigAbort_expectThreadIsTerminated() throws Exception {
        // arrange
        SimpleEventDispatcherImpl eventDispatcher = new SimpleEventDispatcherImpl();
        EventDispatcherThread eventDispacherThread = new EventDispatcherThread( eventDispatcher );

        eventDispacherThread.start();
        waitForThread( eventDispacherThread );

        // act
        eventDispatcher.dispatchEvent( new FSqrThreadSigAbortRequestEvent() );

        // assert
        eventDispacherThread.join();

        State result = eventDispacherThread.getState();
        assertThat( result, equalTo( State.TERMINATED ) );
    }

    @Test
    public void testRun_EventDispatcherStartDispatchSigAbort_expectEventDispatcherNotInvokedForSigAbort() throws Exception {
        // arrange
        SimpleEventDispatcherImpl eventDispatcher = Mockito.spy( new SimpleEventDispatcherImpl() );
        EventDispatcherThread eventDispacherThread = new EventDispatcherThread( eventDispatcher );

        eventDispacherThread.start();
        waitForThread( eventDispacherThread );

        // act
        eventDispatcher.dispatchEvent( new FSqrThreadSigAbortRequestEvent() );

        // assert
        eventDispacherThread.join();

        Mockito.verify( eventDispatcher, times( 0 ) ).handleEvent( Mockito.any() );
    }

    @Test
    public void testRun_EventDispatcherStartDispatchEventThenDispatchSigAbort_expectEventDispatcherOnceInvoked() throws Exception {
        // arrange
        SimpleEventDispatcherImpl eventDispatcher = Mockito.spy( new SimpleEventDispatcherImpl() );
        EventDispatcherThread eventDispacherThread = new EventDispatcherThread( eventDispatcher );

        FSqrEvent event1 = new FSqrEvent() {
        };

        eventDispacherThread.start();
        waitForThread( eventDispacherThread );

        // act
        eventDispatcher.dispatchEvent( event1 );
        eventDispatcher.dispatchEvent( new FSqrThreadSigAbortRequestEvent() );

        // assert
        eventDispacherThread.join();
        Mockito.verify( eventDispatcher, times( 1 ) ).handleEvent( event1 );
    }

    @Test
    public void testRun_EventDispatcherStartDispatchTwoEventsThenDispatchSigAbort_expectEventDispatcherOnceInvoked() throws Exception {
        // arrange
        SimpleEventDispatcherImpl eventDispatcher = Mockito.spy( new SimpleEventDispatcherImpl() );
        EventDispatcherThread eventDispacherThread = new EventDispatcherThread( eventDispatcher );

        FSqrEvent event1 = new FSqrEvent() {
        };
        FSqrEvent event2 = new FSqrEvent() {
        };

        System.out.println( event1 );
        System.out.println( event2 );

        eventDispacherThread.start();
        waitForThread( eventDispacherThread );

        // act
        eventDispatcher.dispatchEvent( event1 );
        eventDispatcher.dispatchEvent( event2 );
        eventDispatcher.dispatchEvent( new FSqrThreadSigAbortRequestEvent() );

        // assert

        eventDispacherThread.join();
        InOrder inOrder = Mockito.inOrder( eventDispatcher );
        inOrder.verify( eventDispatcher, times( 1 ) ).handleEvent( event1 );
        inOrder.verify( eventDispatcher, times( 1 ) ).handleEvent( event2 );

    }

    public void waitForThread( EventDispatcherThread eventDispacherThread ) throws InterruptedException {
        while (eventDispacherThread.getState() == State.NEW) {
            Thread.sleep( 10 );
        }
        while (eventDispacherThread.getState() != State.RUNNABLE) {
            Thread.sleep( 10 );
        }
    }

}
