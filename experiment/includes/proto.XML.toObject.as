/*
# $Id$
# $Name$
#
# James Gauld
# http://www.iamradiator.net/
#
# Build a native Flash Object from the given XML data whose nodes can then be
# accessed using 'dot-syntax'.
#
# NOTES:
# - To force a node to be stored as an array, use the attribute 'XAforce="1"'
#   (you don't need to do this if there are already mutliple nodes of the same type)
# - All whitespace (\n,\r,\t,etc) before and after a node's content will be included
#   in the final object. Set the 'removeWS' to true to remove this whitespace.
# - If a node contains attributes and character data (CDATA), then the character data
#   is moved to an element named '!CDATA' in the same scope as the attribute elements.
*/

//-------------------------------
// Dependancies
//-------------------------------
#include "includes/func.Object.as"

//-------------------------------
// toObject(removeWS)
// bool removeWS - remove leading/trailing whitespace from node CDATA
//-------------------------------
XML.prototype.toObject = function(removeWS) {

	// Init
	removeWS = (removeWS==null || !removeWS) ? false : true;
	var xObj = this;
	var obj = {};
	var arrayNodes = new Object();			// Holds a list of nodes that must be parsed as arrays
	var i;

	// Process tree
	if(xObj.nodeType==1) {

		// Add attributes
		for(i in xObj.attributes) {
			if(i!='XAforce') obj[i] = xObj.attributes[i];
		}

		// Reverse order of children
		// This is a way around the fact that a reverse for(;;) loop doesn't appear to work.
		var cNodes = new Array();
		for(i in xObj.childNodes) {
			cNodes.unshift(xObj.childNodes[i]);
		}

		// Cycle through child nodes
		for(i in cNodes) {

			// Skip children that are just whitespace between nodes
			// This is a bit of an inefficient way to do it - maybe needs re-thinking.
			var s = cNodes[i].toString();
			s = s.split("\n").join("");
			s = s.split("\r").join("");
			s = s.split("\t").join("");
			if(s=="") continue;

			// Vars
			var child = cNodes[i];
			child.toObject = XML.prototype.toObject;		// Need to manually add this function to the child node

			// Add this node to the object
			if(child.nodeName==null) {

				// Move node content into !CDATA element if attributes are already present
				if(object_length(obj)<=0) {
					obj = child.toObject(removeWS);
				}
				else {
					obj['!CDATA'] = child.toObject(removeWS);
				}
			}
			else if(obj[child.nodeName]!=null) {

				// Check if this node is already marked as being an array
				if(arrayNodes[child.nodeName]==null) {
					arrayNodes[child.nodeName] = true;
					obj[child.nodeName] = [obj[child.nodeName]];
				}

				// Push node onto existing array
				obj[child.nodeName][obj[child.nodeName].length] = child.toObject(removeWS);	// Using '.push()' won't work here???
					
			}
			else if(child.attributes['XAforce']!=null) {

				// Check if this node is already marked as being an array
				if(arrayNodes[child.nodeName]==null) {
					obj[child.nodeName] = new Array();
					arrayNodes[child.nodeName] = true;
				}

				// Push node onto existing array
				obj[child.nodeName][obj[child.nodeName].length] = child.toObject(removeWS);
				
			}
			else {

				// Process next tree
				obj[child.nodeName] = child.toObject(removeWS);
			}
		}
	}

	// Store node content
	if(xObj.nodeType==3) {

		// Make sure node value is treated as a character data node
		var nValue = new XML("<![CDATA[" + xObj.nodeValue + "]]>");
		nValue = nValue.toString();

		// Remove leading and trailing whitespace
		if(removeWS) {

			// Leading
			var count = 0;
			var char = nValue.charCodeAt(count);
			while(char<32 && count<nValue.length) {
				count++;
				char = nValue.charCodeAt(count);
			}
			nValue = nValue.substr(count);

			// Trailing
			count = nValue.length-1;
			char = nValue.charCodeAt(count);
			while(char<32 && count>0) {
				count--;
				char = nValue.charCodeAt(count);
			}
			nValue = nValue.substr(0, count+1);
		}

		// Assign value to object
		obj = nValue;
	}

	// Return object
	return obj;
}

