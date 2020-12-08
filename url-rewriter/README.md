1. Install Proxy Servlets  
See provided web.xml

2. Update process definition to take, as contract, the fields that were sent back by external forms  
Example:  

V6 Page flow configuration:  
- validAsText (data) Takes value of field_valid  
- requestDescription Takes value of field_description  

becomes:  
Execution > Contract >  
- validInput TEXT  
- descriptionInput TEXT  

Execution > Operations >  
- valid Takes value of validInput  
- requestDescription Takes value of descriptionInput   

3. Update process definition to define the URLs to redirect to  
On each task/processwith external form:  
- Execution > Form >  
    External URL: /bonita/forms/v6/proxy  
(Whatever the URL you used to have, it will now all be the same value to point to the Proxy)  
- Declare a task/process variable named externalFormURL which content is a groovy expression returning a URL of the form to display for the task.  
To this URL the id (containing the task id) and submitURL (URL used by your external form to submit the task) parameters will be added at runtime by the proxy (this replicates the v6 redirect URL feature behavior).  

In my example the form name: validateRequestForm  
In my example I used a process parameter "externalFormServerURL" to store the URL of the server hosting forms (http://127.0.0.3:8888/)  
