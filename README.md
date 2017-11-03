# Consigna:

Existe un call center donde hay 3 tipos de empleados: operador, supervisor y director. El proceso de la atención de una llamada telefónica en primera instancia debe ser atendida por un operador, si no hay ninguno libre debe ser atendida por un supervisor, y de no haber tampoco supervisores libres debe ser atendida por un director.


# Notas:

Utilizo tres clases para modelar el ejercicio, <b>Distpacher</b> con la responsabilidad de asignar llamadas a los empleados, <b>Call</b> para las llamadas, las cuales pueden tener diferentes estados (NEW, WAITING, ATTENDED, FINISHED) y <b>Employer</b>, para los empleados, que pueden ser de tipo OPERADOR, SUPERVISOR o DIRECTOR, y pueden estar en estado o AVAILABLE o ASSIGNED.


Para manejar llamadas de forma concurrente uso <b>ExecutorService</b> seteando un pool de 10 Threads.

Cuando llegan más llamadas concurrentes de las que puedo recibir, voy poniendolas en espera utilizando una cola, utilizando <b>ConcurrentLinkedQueue</B>, a  espera de que haya un thread disponible.


En caso de que tenga menos emplados que llamadas, pongo las mismas en una cola hasta que se libere un empleado.


# Tecnologías usadas

[Java 8](https://www.java.com/es/download/faq/java8.xml)

[Maven](https://maven.apache.org)


# Referencias

[ExecutorService](https://www.arquitecturajava.com/java-executor-service-y-threading/)

[ConcurrentLinkedQueue](https://www.redeszone.net/2012/10/01/curso-java-volumen-ix-estructuras-para-programacion-concurrente-ii/)

[Funciones lambda y stream en Java 8](http://www.oracle.com/technetwork/es/articles/java/expresiones-lambda-api-stream-java-2633852-esa.html)

[Clase Optional de java 8](https://www.adictosaltrabajo.com/tutoriales/optional-java-8/)



