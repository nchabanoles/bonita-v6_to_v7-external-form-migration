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

http://localhost:24393/bonita/forms/v6/proxy?id=40016&description=itWalksFolks!
