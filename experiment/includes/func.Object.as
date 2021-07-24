/*
# $Id$
# $Name$
#
# James Gauld
# http://www.iamradiator.net/
#
# An assortment of useful functions for the Object type.
*/

//-------------------------------
// Dependancies
//-------------------------------
// none

//-------------------------------
// object_length(obj)
// Return the number of elements in the specified object
//-------------------------------
_global.object_length = function(obj) {

	// Count objects within this object
	var len = 0;
	for(i in obj) len++;

	//Result
	return len;
}
