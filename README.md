Note: This project was built entirely by me from scratch. 
While the Git history may not reflect regular commits during development, 
every line of code in this repository was written and understood by me personally. 
I chose to focus deeply on building and learning during the process, and as such, didn’t maintain a granular commit history. 
I believe in transparency, and I’m always happy to walk through the project to explain the architecture, logic, and decisions involved.
However the future any features or changes will be regularly committed.


ABOUT PROJECT :
This is a Spring Boot-based OTP (One-Time Password) service that allows users to generate and validate OTPs via RESTful APIs. 
It supports custom OTP lengths and types (numeric, alphabetic, alphanumeric), and automatically handles OTP expiration and cleanup. 
The service includes email integration to send OTPs and provides a /validate endpoint for secure verification. Expired OTPs are removed periodically using a scheduled task. 
Errors and edge cases are handled globally with @RestControllerAdvice.
