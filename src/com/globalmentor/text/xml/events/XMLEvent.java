package com.globalmentor.text.xml.events;

import java.util.Date;
import org.w3c.dom.events.*;

/**The class which represents an XML event.
@see com.globalmentor.text.xml.XMLNode
@author Garret Wilson
*/
public class XMLEvent implements Event
{

	/**A custom phase indicating the event has not yet been dispatched.*/
	public static final short BEFORE_DISPATCH_PHASE=0;

	/**@return <code>true</code> if the event entered one of the dispatch phases.*/
	public boolean isDispatched() {return getEventPhase()!=BEFORE_DISPATCH_PHASE;}

	/**The name of the event (case-insensitive). The name must be an XML name.*/
	private String type="";

		/**@return The name of the event (case-insensitive), which must be an XML name.*/
		public String getType() {return type;}

		/**Sets the type of the event.
		@param newType The event's new type.
		*/
	  public void setType(final String newType) {type=newType;}

	/**Indicates the target to which the event was originally dispatched.*/
	private EventTarget target=null;

		/**@return The target to which the event was originally dispatched.*/
    public EventTarget getTarget() {return target;}

		/**Sets the target to which the event was originally dispatched.
		@param newTarget The target to which the event was originally dispatched.
	  */
    public void setTarget(final EventTarget newTarget) {target=newTarget;}

	/**The target whose event listeners are currently being processed.*/
	private EventTarget currentTarget=null;

		/**@return The target whose event listeners are currently being processed;
		  used, for example, during capturing and bubbling.
		*/
    public EventTarget getCurrentTarget() {return currentTarget;}

		/**Sets the target whose event listeners are currently being processed. This
		  is particularly useful during capturing and bubbling.
		@param newCurrentTarget The target whose event listeners are currently being processed.
		*/
    public void setCurrentTarget(final EventTarget newCurrentTarget) {currentTarget=newCurrentTarget;}

	/**The phase of event flow.*/
	private short eventPhase=BEFORE_DISPATCH_PHASE;

		/**@return The phase of event flow currently being evaluated.*/
		public short getEventPhase() {return eventPhase;}

		/**Sets phase of event flow currently being evaluated.
		@param newEventPhase The phase of event flow currently being evaluated.
		*/
		public void setEventPhase(short newEventPhase) {eventPhase=newEventPhase;}

	/**Whether or not an event is a bubbling event.*/
	private boolean bubbles=false;

		/**Indicates whether or not an event is a bubbling event.*/
    public boolean getBubbles() {return bubbles;}

		/**Sets whether or not bubbling is allowed. External classes should call
		  <code>initEvent</code>, which will ensure this value is not modified for
			certain classes which do not allow bubbling.
		@param newBubbles Whether or not bubbling should be allowed.
		*/
		protected void setBubbles(final boolean newBubbles) {bubbles=newBubbles;}

	/**Whether or not an event is cancelable.*/
	private boolean cancelable=false;

		/**Indicates whether or not an event can have its default action
		  prevented.*/
    public boolean getCancelable() {return cancelable;}

		/**Sets whether or not the event can be canceled. External classes should call
		  <code>initEvent</code>, which will ensure this value is not modified for
			certain classes which do not allow canceling.
		@param newCancelable Whether or not canceling should be allowed.
		*/
		protected void setCancelable(final boolean newCancelable) {cancelable=newCancelable;}

	/**The date the event was created.*/
	private Date date=new Date();

	/**@return Tthe time (in milliseconds relative to 0:0:0 UTC 1st January 1970)
		at which the event was created.
	*/
	public long getTimeStamp() {return date.getTime();}

	/**Whether the event is currently propagating.*/
	private boolean propagating=true;

		/**@return Whether the event is currently propagating.*/
		public boolean isPropagating() {return propagating;}

		/**Sets whether the event is currently propagating.
		@param newPropagating Whether the event should be propagating.
		@see #stopPropagation
		*/
		private void setPropagating(final boolean newPropagating) {propagating=newPropagating;}

	/**Prevents further propagation of an event during event flow. If this method
		is called by any <code>EventListener</code> the event will cease propagating
		through the tree. The event will complete dispatch to all listeners
		on the current <code>EventTarget</code> before event flow stops. This
		method may be used during any stage of event flow.
	@see #setPropagating
	*/
	public void stopPropagation()
	{
		setPropagating(false);  //turn off propagation
	}

	/**Whether the event's default action is occurring.*/
	private boolean defaultActionOccurring=true;

		/**@return Whether the event's default action is occurring.*/
		public boolean isDefaultActionOccurring() {return defaultActionOccurring;}

		/**Sets whether the event's default action is occurring.
		@param newDefaultActionOccurring Whether the event's default action is occurring.
		@see #preventDefault
		*/
		private void setDefaultActionOccurring(final boolean newDefaultActionOccurring) {defaultActionOccurring=newDefaultActionOccurring;}

	/**If an event is cancelable, the <code>preventDefault</code> method is
		used to signify that the event is to be canceled, meaning any default
		action normally taken by the implementation as a result of the event
		will not occur. If, during any stage of event flow, the
		<code>preventDefault</code> method is called the event is canceled.
		Any default action associated with the event will not occur. Calling
		this method for a non-cancelable event has no effect. Once
		<code>preventDefault</code> has been called it will remain in effect
		throughout the remainder of the event's propagation. This method may
		be used during any stage of event flow.
	@see #setDefaultActionOccurring
	*/
	public void preventDefault()
	{
		if(getCancelable()) //if this event can be cancelled
			setDefaultActionOccurring(false); //stop the default action from occurring
	}

	/**Used to initialize the value of an <code>Event</code> created through the
		<code>DocumentEvent</code> interface. This method may only be called before the
		<code>Event</code> has been dispatched via the <code>dispatchEvent</code>
		method, though it may be called multiple times during that phase if necessary.
		If called multiple times the final invocation takes precedence.
		If called from a subclass of <code>Event</code> interface only the values
		specified in the <code>initEvent</code> method are modified, all other
		attributes are left unchanged.
	@param eventTypeArg The event type. This type may be any event type currently
		defined in this specification or a new event type. The string must be an
		XML name. Any new event type must not begin with any upper, lower, or mixed
		case version of the string "DOM". This prefix is reserved for future DOM
		event sets. It is also strongly recommended that third parties adding their
		own events use their own prefix to avoid confusion and lessen the
		probability of conflicts with other new events.
	@param canBubbleArg Whether or not the event can bubble.
	@param cancelableArg Whether or not the event's default action can be prevented.
	*/
	public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
	{
		if(!isDispatched())  //only allow initialization if the event has not yet been dispatched
		{
			setType(eventTypeArg);  //set the event type
			setBubbles(canBubbleArg); //set whether or not this event can bubble
			setCancelable(cancelableArg); //set whether this event can be cancelled
		}
	}

}