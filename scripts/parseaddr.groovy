/*
 * Replaces characters in address string to make API query URL
 */

def flowFile = session.get()

if (flowFile == null) {
    return
}

def addr = flowFile.getAttribute("address")

// replace characters with url-accepted codes
addr = addr.replaceAll(" ", "%20")
addr = addr.replaceAll(",", "%2C")
addr = addr.replaceAll("\"", "%22")
addr = addr.replaceAll("#", "%23")
    // can add others as needed

flowFile = session.putAttribute(flowFile, "address", addr)

session.transfer(flowFile, REL_SUCCESS)
