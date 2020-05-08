Install Proxy Servlets
See provided web.xml

update process definition to take as contract, the fields that were sent back by external forms
example:
V6 Page flow configuration:
    validAsText (data) Takes value of field_valid
    requestDescription Takes value of field_description
becomes Execution > Contract >
    validInput TEXT
    descriptionInput TEXT
and
Execution > Operations
    valid Takes value of validInput
    requestDescription Takes value of descriptionInput
and
Execution > Form
    External URL /bonita/forms/v6/proxy
(Whatever the URL you used to have, it will now all be the same value to point to the Proxy)

Declare on each human task with external form a variable named externalFormURL
which content is a groovy expression returning a URL of the form to display for the task. To this URL the id (containing
 the task id) and submitURL (URL used by your external form to submit the task) parameters will be added at runtime par
 Bonita

In my example the form name: validateRequestForm

In my example I used a process parameter to store the URL of the server hosting forms
externalFormServerURL with the URL of the endpoint serving the form http://127.0.0.3:8888/

http://localhost:24393/bonita/forms/v6/proxy?id=40016&description=itWalksFolks!
