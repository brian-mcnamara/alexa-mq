# ConnectedMQ

Beginnings of project to have oauth connecting to external service such as Amazon (alexa) to post messages
to a destination and later retrieve those messages via another client (destination) (raspberry pi, etc) to dequeue the message
and run its own logic (such as local connected devices with intranet APIs). Clients can connect via user access token
allowing access to APIs including REST and SSE.

Other thoughts, I need a service discovery service that allows clients (destinations) to register their themselves
and provide context on Alexa commands but thats really its own project.

Using Spring for many features such as oauth, authentication an beans of course.