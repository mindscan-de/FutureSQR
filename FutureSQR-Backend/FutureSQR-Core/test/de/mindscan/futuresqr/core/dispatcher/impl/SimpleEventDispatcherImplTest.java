package de.mindscan.futuresqr.core.dispatcher.impl;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.futuresqr.core.events.FSqrEvent;
import de.mindscan.futuresqr.core.queue.ThreadBoundArrayDeque;

public class SimpleEventDispatcherImplTest {

    @Test
    public void testSimpleEventDispatcherImpl_CTorOnly_NoException() throws Exception {
        // arrange
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();

        // act
        // assert
    }

    @Test
    public void testDispatchEvent_dispatchNullEvent_doesntThrowIllegalStateException() throws Exception {
        // arrange
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();

        // act
        // assert
        dispatcher.dispatchEvent( null );
    }

    @Test
    public void testDispatchEvent_dispatchNonNullEventEventQueueNotInitialized_throwsIllegalStateExceltipon() throws Exception {
        // arrange
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();
        FSqrEvent event = new FSqrEvent() {
        };

        // act
        // assert
        Assertions.assertThrows( IllegalStateException.class, () -> dispatcher.dispatchEvent( event ) );
    }

    @Test
    public void testDispatchEvent_dispatchNonNullEventQueueInitialited_invokesAddOnQueue() throws Exception {
        // arrange
        @SuppressWarnings( "unchecked" )
        ThreadBoundArrayDeque<FSqrEvent> eventQueue = Mockito.mock( ThreadBoundArrayDeque.class, "eventqueue" );
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();
        dispatcher.setEventQueue( eventQueue );

        FSqrEvent expectedEvent = new FSqrEvent() {
        };

        // act
        dispatcher.dispatchEvent( expectedEvent );

        // assert
        Mockito.verify( eventQueue, times( 1 ) ).add( expectedEvent );
    }

    @Test
    public void testDispatchEvent_dispatchNullEventEventQueueInitialized_doesntInvokeAddOnQueue() throws Exception {
        // arrange
        @SuppressWarnings( "unchecked" )
        ThreadBoundArrayDeque<FSqrEvent> eventQueue = Mockito.mock( ThreadBoundArrayDeque.class, "eventqueue" );
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();
        dispatcher.setEventQueue( eventQueue );

        // act
        dispatcher.dispatchEvent( null );

        // assert
        Mockito.verify( eventQueue, times( 0 ) ).add( Mockito.any() );

    }

}
