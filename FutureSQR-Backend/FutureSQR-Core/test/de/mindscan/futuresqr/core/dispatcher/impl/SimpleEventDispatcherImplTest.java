package de.mindscan.futuresqr.core.dispatcher.impl;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.futuresqr.core.events.FSqrEvent;
import de.mindscan.futuresqr.core.events.FSqrEventListener;
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

    @Test
    public void testHandleEvent_NullEvent_throwsNoException() throws Exception {
        // arrange
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();

        // act
        // assert
        dispatcher.handleEvent( null );
    }

    @Test
    public void testHandleEvent_NonNullEventNoRegisteredHandler_throwsNoException() throws Exception {
        // arrange
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();
        FSqrEvent nonNullEvent = new FSqrEvent() {
        };

        // act
        // assert
        dispatcher.handleEvent( nonNullEvent );
    }

    @Test
    public void testHandleEvent_NonNullEventRegisteredEvent_invokesHandlerOnce() throws Exception {
        // arrange
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();
        FSqrEvent expectedEvent = new FSqrEvent() {
        };
        FSqrEventListener listener = Mockito.mock( FSqrEventListener.class, "listener" );

        dispatcher.registerEventListener( expectedEvent.getClass(), listener );

        // act
        dispatcher.handleEvent( expectedEvent );

        // assert
        Mockito.verify( listener, times( 1 ) ).handleEvent( expectedEvent );
    }

    @Test
    public void testHandleEvent_AnonymousEventRegisteredAnonymousClassAndBaseClass_invokesHandlerOnce() throws Exception {
        // arrange
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();
        FSqrEvent expectedEvent = new FSqrEvent() {
        };
        FSqrEventListener listener = Mockito.mock( FSqrEventListener.class, "listener" );

        dispatcher.registerEventListener( expectedEvent.getClass(), listener );
        dispatcher.registerEventListener( FSqrEvent.class, listener );

        // act
        dispatcher.handleEvent( expectedEvent );

        // assert
        Mockito.verify( listener, times( 1 ) ).handleEvent( expectedEvent );
    }

    @Test
    public void testRegisterEventListener_ClassNull_throwsIllegalArgumentException() throws Exception {
        // arrange
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();
        FSqrEventListener listener = Mockito.mock( FSqrEventListener.class, "listener" );

        // act
        // assert
        Assertions.assertThrows( IllegalArgumentException.class, () -> dispatcher.registerEventListener( null, listener ) );
    }

    @Test
    public void testRegisterEventListener_ListenerNull_throwsIllegalArgumentException() throws Exception {
        // arrange
        SimpleEventDispatcherImpl dispatcher = new SimpleEventDispatcherImpl();

        // act
        // assert
        Assertions.assertThrows( IllegalArgumentException.class, () -> dispatcher.registerEventListener( FSqrEvent.class, null ) );
    }

    // TODO work with all base classes...

}
