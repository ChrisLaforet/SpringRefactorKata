Spring Refactor Kata

This code suffers from a lack of test instrumentation.  You have just received it to accomplish
a few goals:

1. There is a critical bug in which Covid serology tests do not appear to be handled
   correctly.  The test number is not being set properly but is showing up as IMM9008
   and the description is showing up as "Novel coronavirus-2019" which is not correct.
   The code appears to handle it but it is wrong somehow.
2. At the same time this emergency fix is going to be released, the business requires
   support for a new type of lab test TUMOR#### for tumor markers.  These tests must
   be imported except for TUMOR0100 through TUMOR0149 which are not relevant for this
   type of patient record. When importing, the test code must be changed to TM followed
   by the number of the original test to match the internal test catalog naming.
3. There occasionally is a NumberFormatException that is returned to the REST client.
   The source of this needs to be found and converted to an IllegalArgumentException.
4. There are no tests, as mentioned earlier. Since the Covid test issue has caused a
   major reputational blow to our clinic, management insists that there needs to be 
   full test coverage around the LoadResultsFromLabCommandHandler.  Additionally, the
   technical managers have demanded that a full test suite be developed to anchor the
   current functionality BEFORE any other work is done to implement fixes or changes.
5. The organization recently installed SonarQube and have a mandate to fix findings
   on a best-effort basis. During this fix, especially since the code has problems,
   there is a request to address the following Sonar findings:

   "Refactor this method to reduce its Cognitive Complexity from 25 to the 15 allowed."
   on mapValidTestResults()

   "This block of commented-out lines of code should be removed." on line 126

Notice that instrumenting tests must expose business logic without exposing internal
functions in LoadResultsFromLabCommandHandler. It is up to you if you want to create
tests that mock external services and/or repositories. We agree that the services and
repositories work correctly since they actually have a full suite of tests (that are
not included here to simplify this project).

There are different approaches to this process, obviously. One is to leave functionality
intact in this handler but consider if this breaks the SRP (Single Responsibility 
Principle) or not and if so, is it worth breaking the principle? The other is to hoist 
code into classes that will preserve the SRP and expose test points.

The clock is ticking for this emergency release. Good luck!

P.S. This code is not a running project in Spring. Imagine that Spring wraps this
logic hence the hints pointing out to components. However, in this kata, your 
tests will exercise the logic.
