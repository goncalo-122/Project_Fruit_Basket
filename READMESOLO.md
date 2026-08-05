##Learning notes
* **Client-Make HTTP requests to another Services
* **Entity-Table dataBase
* **Repository-Access to dataBase
* **DTO-Data Transfer Object->objeto usado para transportar dados entre camadas, sem conter regras de negócio.
* **REST-Where HTTP endpoints are defined
* **Service-Business logic
* **Resource = Conection with the exterior

##DTO CLASSES IN BASKET
* **BasketDTO → represents a complete basket
* **BasketItemDTO → represents ONE item with in the basket
* **FruitDTO → represents the fruit coming from the Fruit microservice.

+ **stream** — Iterates through a list of entities
+ **.map(e → { })** — Transforms each entity into a BasketItemDTO
  f1.fruitId = 1L;- 1 Long

+ Integration Test-Tests various parts of the system working together.
+ Unit Test-Tests only a single, isolated unit of code, like a method of a class.

| Código                             | Significado                                                                         |
|------------------------------------|-------------------------------------------------------------------------------------|
| ``assertThrows``                   | Espera que uma exceção seja lançada(Caso nao seja, o teste falha)                   |
| ``IllegalArgumentException.class`` | Tipo de exceção esperada                                                            |
| ``() ``-> ``...``                  | Método que deve lançar a exceção          ex:() -> service.addFruits(List.of(f1))); |
|


verify(fruitClient).updateFruitQuantity(eq(10L), any(FruitDTO.class));-> eq needs to be the first parameter


Docker commands

docker images-List every image installed
docker pull name:tag-Download images from Docker HUB
docker build -t name:version-Create an image from Docker file
docker rmi name:version-Remove image
docker run name:version-Create and start container
docker run -d name:version-Create and start container in background
docker ps: List containers that are running now
docker ps -a : List containers that are running now and stopped
docker stop id: Stop a container
docker rm id: Remove a container


docker compose up -d : Start every service defined in docker-compose.yml
docker compose down: Stop and remove containers from compose
docker compose logs: Show every log from every services

DOCKER FILE:
  FROM-Image base(Java, Ubuntu, etc)
  COPY-Copy files to inside image
  WORKDIR-Define a folder where commands will run
  EXPOSE-PORT THAT APP IS EXPOSED
  CMD-COMMAND THAT STARTS THE APP
