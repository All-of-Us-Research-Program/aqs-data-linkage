/*
 * Splitting date range into 24 hour increments ensures that the API limit
 * for number of lines in a request is not reached.
 * All requested dates are still preserved, just split into more flowfiles.
 */

import java.text.SimpleDateFormat

def flowFile = session.get();

if (flowFile == null) {
    return
}

try {
    def bdate = flowFile.getAttribute("begin")
    def edate = flowFile.getAttribute("end")

    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd")
    TimeZone utcZone = TimeZone.getTimeZone("America/New_York")
    inputDateFormat.setTimeZone(utcZone)

    SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyyMMdd")
    TimeZone outputZone = TimeZone.getTimeZone("America/New_York")
    outputDateFormat.setTimeZone(outputZone)

    Date prev = inputDateFormat.parse(bdate)
    Date late = inputDateFormat.parse(edate)
    Date tmp1 = prev
    Date tmp2 = prev.next()

    while(tmp2.before(late)) {

        def newFF = session.create(flowFile)
        newFF = session.putAllAttributes(newFF, [
            "bdate": outputDateFormat.format(tmp1),
            "edate": outputDateFormat.format(tmp2)
        ])
        session.transfer(newFF, REL_SUCCESS)

        tmp1 = tmp2
        tmp2 = tmp2.next()
    }

    flowFile = session.putAllAttributes(flowFile, [
        "bdate": outputDateFormat.format(tmp1),
        "edate": outputDateFormat.format(late)
    ])
    session.transfer(flowFile, REL_SUCCESS)

} catch (Exception ex) {
    flowFile = session.putAttribute(flowFile, "datetime.error", ex.getMessage())
    session.transfer(flowFile, REL_FAILURE)
}