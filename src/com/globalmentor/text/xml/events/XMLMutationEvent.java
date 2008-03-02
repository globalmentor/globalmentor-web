package com.globalmentor.text.xml.events;

import org.w3c.dom.Node;
import org.w3c.dom.events.*;

import com.globalmentor.text.xml.XMLAttribute;
import com.globalmentor.text.xml.XMLNode;

/**Represents a change in the document tree.
@author Garret Wilson
*/
public class XMLMutationEvent extends XMLEvent implements MutationEvent
{

	/**The identification string of this class of events.*/
	public final static String FEATURE_NAME="MutationEvents";

	//types of mutation events
	public final static String DOM_SUBTREE_MODIFIED="DOMSubtreeModified";
	public final static String DOM_NODE_INSERTED="DOMNodeInserted";
	public final static String DOM_NODE_REMOVED="DOMNodeRemoved";
	public final static String DOM_NODE_REMOVED_FROM_DOCUMENT="DOMNodeRemovedFromDocument";
	public final static String DOM_NODE_INSERTED_INTO_DOCUMENT="DOMNodeInsertedIntoDocument";
	public final static String DOM_ATTR_MODIFIED="DOMAttrModified";
	public final static String DOM_CHARACTER_DATA_MODIFIED="DOMCharacterDataModified";

	/**The node related to this mutation, if applicable.*/
	private Node relatedNode=null;

	/**Used to identify a secondary node related  to a mutation event.
		For example, if a mutation event is dispatched to a node indicating that
		its parent has changed, the <code>relatedNode</code> is the changed parent.
		If an event is instead dispatched to a subtree indicating a node was changed
		within it, the <code>relatedNode</code> is the changed node. In the case of
		the DOMAttrModified event it indicates the <code>Attr</code> node
		which was modified, added, or removed.
	@return A related node, where applicable.
	*/
	public Node getRelatedNode() {return relatedNode;}

	/**The previous value for an attribute or character data modification.*/
	private String prevValue=null;

	/**@return The previous value of the <code>Attr</code> node in DOMAttrModified
		events, or of the<code>CharacterData</code> node in DOMCharDataModified events,
		or <code>null</code> if not applicable.
	*/
	public String getPrevValue() {return prevValue;}

	/**The new value for an attribute or character data modification.*/
	private String newValue=null;

	/**@return The new value of the <code>Attr</code> node in DOMAttrModified events,
		or of the <code>CharacterData</code> node in DOMCharDataModified events,
		or <code>null</code> if not applicable.
	*/
	public String getNewValue() {return newValue;}

	/**The new attribute value for an attribute.*/
	private String attrName=null;

	/**@return The name of the changed <code>Attr</code> node in a DOMAttrModified event,
		or <code>null</code> if not applicable.
	*/
	public String getAttrName() {return attrName;}

	/**The type of change which triggered the event from modifying an attribute.*/
	private short attrChange=0;

	/**@return The type of change which triggered the DOMAttrModified event.
		The values can be <code>MODIFICATION</code>, <code>ADDITION</code>, or
		<code>REMOVAL</code>, or <code>0</code> if not applicable.
	*/
	public short getAttrChange() {return attrChange;}

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
		This is ignored, as no mutation events can be cancelable
	*/
	public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg)
	{
		super.initEvent(eventTypeArg, canBubbleArg, false); //never allow mutation events to be cancelable
	}

	/**Initializes the value of a <code>MutationEvent</code> created through the
		<code>DocumentEvent</code> interface. This method may only be called
		before the <code>MutationEvent</code> has been dispatched via the
		<code>dispatchEvent</code> method, though it may be called multiple
		times during that phase if necessary. If called multiple times, the
		final invocation takes precedence.
	@param typeArg The event type.
	@param canBubbleArg Whether or not the event can bubble.
	@param cancelableArg Whether or not the event's default action can be prevented.
	@param relatedNodeArg The <code>Event</code>'s related Node.
	@param prevValueArg The <code>Event</code>'s <code>prevValue</code> attribute.
		This value may be <code>null</code>.
	@param newValueArg The <code>Event</code>'s <code>newValue</code> attribute.
		This value may be <code>null</code>.
	@param attrNameArg The <code>Event</code>'s <code>attrName</code> attribute.
		This value may be <code>null</code>.
	@param attrChangeArg The <code>Event</code>'s <code>attrChange</code> attribute.
	*/
	public void initMutationEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg,
		  Node relatedNodeArg, String prevValueArg, String newValueArg, String attrNameArg,
			short attrChangeArg)
	{
		initEvent(typeArg, canBubbleArg, cancelableArg);  //do the default initialization
		relatedNode=relatedNodeArg; //set the related node
		prevValue=prevValueArg; //set the previous value
		newValue=newValueArg; //set the new value
		attrName=attrNameArg; //set the attribute name
		attrChange=attrChangeArg;  //set the attribute change type
	}

	/**Creates a general event for document modification.
	@param lowestCommonParent The lowest common parent of the changes that have taken place
	@return The new event.
	*/
	public static XMLMutationEvent createDOMSubtreeModifiedEvent(final XMLNode lowestCommonParent)
	{
		final XMLMutationEvent xmlMutationEvent=new XMLMutationEvent();  //create a new mutation event
		xmlMutationEvent.initMutationEvent(DOM_SUBTREE_MODIFIED, true, false, null, null, null, null, (short)0);
		xmlMutationEvent.setTarget(lowestCommonParent);  //show which node is the target
		return xmlMutationEvent;  //return the event we created
	}

	/**Creates an event for node insertion.
	@param insertedNode The node being inserted.
	@param parentNode The parent node of the inserted node.
	@return The new event.
	*/
	public static XMLMutationEvent createDOMNodeInsertedEvent(final XMLNode insertedNode, final XMLNode parentNode)
	{
		final XMLMutationEvent xmlMutationEvent=new XMLMutationEvent();  //create a new mutation event
		xmlMutationEvent.initMutationEvent(DOM_NODE_INSERTED, true, false, parentNode, null, null, null, (short)0);
		xmlMutationEvent.setTarget(insertedNode);  //show which node is the target
		return xmlMutationEvent;  //return the event we created
	}

	/**Creates an event for node removal.
	@param removedNode The node being removed.
	@param parentNode The parent node of the removed node.
	@return The new event.
	*/
	public static XMLMutationEvent createDOMNodeRemovedEvent(final XMLNode removedNode, final XMLNode parentNode)
	{
		final XMLMutationEvent xmlMutationEvent=new XMLMutationEvent();  //create a new mutation event
		xmlMutationEvent.initMutationEvent(DOM_NODE_REMOVED, true, false, parentNode, null, null, null, (short)0);
		xmlMutationEvent.setTarget(removedNode);  //show which node is the target
		return xmlMutationEvent;  //return the event we created
	}

	/**Creates an event for node removal from the document.
	@param removedNode The node being removed from the document.
	@return The new event.
	*/
	public static XMLMutationEvent createDOMNodeRemovedFromDocumentEvent(final XMLNode removedNode)
	{
		final XMLMutationEvent xmlMutationEvent=new XMLMutationEvent();  //create a new mutation event
		xmlMutationEvent.initMutationEvent(DOM_NODE_REMOVED_FROM_DOCUMENT, false, false, null, null, null, null, (short)0);
		xmlMutationEvent.setTarget(removedNode);  //show which node is the target
		return xmlMutationEvent;  //return the event we created
	}

	/**Creates an event for node insertion into the document.
	@param removedNode The node being inserted into the document.
	@return The new event.
	*/
	public static XMLMutationEvent createDOMNodeInsertedIntoDocumentEvent(final XMLNode insertedNode)
	{
		final XMLMutationEvent xmlMutationEvent=new XMLMutationEvent();  //create a new mutation event
		xmlMutationEvent.initMutationEvent(DOM_NODE_INSERTED_INTO_DOCUMENT, false, false, null, null, null, null, (short)0);
		xmlMutationEvent.setTarget(insertedNode);  //show which node is the target
		return xmlMutationEvent;  //return the event we created
	}

	/**Creates an event for attribute modification.
	@param modifiedNode The node the attribute of which has changed.
	@param modifiedAttribute The attribute that changed
	@param attrName The name of the modified node.
	@param attrChange The type of change.
	@param prevValue The previous attribute value.
	@param newValue The new attribute value.
	@return The new event.
	*/
	public static XMLMutationEvent createDOMAttrModifiedEvent(final XMLNode modifiedNode, final XMLAttribute modifiedAttribute,
		  final String attrName, final short attrChange, final String prevValue, final String newValue)
	{
		final XMLMutationEvent xmlMutationEvent=new XMLMutationEvent();  //create a new mutation event
		xmlMutationEvent.initMutationEvent(DOM_NODE_INSERTED, true, false, modifiedAttribute, prevValue, newValue, attrName, attrChange);
		xmlMutationEvent.setTarget(modifiedNode);  //show which node is the target
		return xmlMutationEvent;  //return the event we created
	}

	/**Creates an event for character data modification.
	@param modifiedNode The character data node that has changed.
	@param prevValue The previous character data value.
	@param newValue The new character data value.
	@return The new event.
	*/
	public static XMLMutationEvent createDOMAttrModifiedEvent(final XMLNode modifiedNode, final String prevValue, final String newValue)
	{
		final XMLMutationEvent xmlMutationEvent=new XMLMutationEvent();  //create a new mutation event
		xmlMutationEvent.initMutationEvent(DOM_CHARACTER_DATA_MODIFIED, true, false, null, prevValue, newValue, null, (short)0);
		xmlMutationEvent.setTarget(modifiedNode);  //show which node is the target
		return xmlMutationEvent;  //return the event we created
	}

}