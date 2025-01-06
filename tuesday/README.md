# welcome to the Tuesday Assignment

Current tasks will help you to practice docker usage.

- [Task 1](#task-1)
- [Task 2](#task-2)
- [Task 3](#task-3)
- [Task 4](#task-4)

## Task 1

Run a simple [httpd:2.4](https://hub.docker.com/_/httpd) container with an exposed `8080` port. To check if it runs
correctly just open `localhost:8080`

- review the logs of the running container
- use `docker exec -it <container_id> bash` to inspect the container. Check the package where sources are placed:
  `/usr/local/apache2/htdocs/`. (useful commands: `pwd`, `ls`, `cat`, to exit use `exit` command)
- update the found `index.html` content with any desired changes.
- verify that the httpd server shows different content according to your changes.

_Hint. To update the content you can either:_

- use `docker cp <source> CONTAINER:<destination>` [command](https://docs.docker.com/reference/cli/docker/container/cp/)
  to copy the file from the host to the container.
- use `cat >> <file> << <your text>` to append text to the end of the existing file. Example:

```bash
cat >> /usr/local/apache2/htdocs/index.html << EOF
<h1>hello world</h1>
EOF
```

## Task 2

Pull and run a custom [nourish1709/httpd:1.0](https://hub.docker.com/repository/docker/nourish1709/httpd/general)
container with exposed `8080` port. To check if it runs correctly open `localhost:8080`

- discover why does the current container return a different response. use `docker exec -it <container> bash` to inspect
  the container. Check the `/usr/local/apache2/htdocs/` and see the resources

## Task 3

Run a [httpd:2.4](https://hub.docker.com/_/httpd) container with resources provided in the [static](./static) folder.
Ensure that the resources are available on the `localhost:8080` endpoint and the response is the same as in the "Task
2". Achieve the desired result by:

- using [Dockerfile](./Dockerfile). Check the [documentation](https://docs.docker.com/reference/dockerfile/) if needed
- using `docker run` along with [volumes](https://docs.docker.com/engine/storage/volumes/#options-for---volume)

Make sure to try both methods provided above

## Task 4

Check the existing [servlet](./servlet) folder. It contains a
simple [HelloServlet](./servlet/src/main/java/com/nourish1709/learning/servlet/HelloServlet.java) that returns a "Hello
World" message. The [index.jsp](./servlet/src/main/webapp/index.jsp) file is used to render the default page and
contains `<a>` reference to the mentioned servlet. The [Dockerfile](./servlet/Dockerfile) is used to deploy the servlet
to a Tomcat container.

Your task is to:

1. Update the [Dockerfile](./servlet/Dockerfile) to deploy the servlet to a Tomcat container.
2. Create an image from the updated Dockerfile and start a container (don't forget to expose the port `8080`). Make sure
   that the servlet is available at `localhost:8080/{artifact}` endpoint.

__Note__ that the destination `.war` filename is used as the base path for the servlet. For example, if you create
a `/usr/local/tomcat/webapps/my_servlet.war` file, the servlet will be available at `localhost:8080/my_servlet/`

3. Add you own servlet named `UserServlet` along with `HelloServlet`. The servlet should return an HTML page with
   current date and your name. Update [index.jsp](./servlet/src/main/webapp/index.jsp) to contain `<a>` reference to the
   new servlet. Deploy the servlet and make sure it works correctly.
