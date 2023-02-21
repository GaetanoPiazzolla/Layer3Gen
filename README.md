# Layer3gen
Generates the standard SPRING 3 layer CRUD architecture starting from JPA entities.
* * *
![sketch](./doc/3layer-sketch.png)
* * *

## How to run as a standalone application:
1. Download and extract the ZIP/TAR file from [releases](https://github.com/GaetanoPiazzolla/Layer3Gen/releases);
2. Edit the file in /bin directory named _3layer-settings.yml_;
3. Run the application (layer3gen.sh or layer3gen.bat).

## How to run as gradle plugin:
1. Add the plugin in your build.gradle;

```groovy
buildscript {
    dependencies {
	classpath "gae.piaz:layer3gen:1.9"
    }
    // ....
}
// ...
apply plugin: 'gae.piaz.layer3gen'
```
or for gradle version above 2.1

```groovy
plugins {
    id "gae.piaz.layer3gen" version "1.8"
}
```
2. Create a file named _3layer-settings.yml_ (you willl find an example below) in the directory src/main/resources/;
3. Run the gradle task.

```shell script
gradlew clean build layer3gen
```

## 3layer-settings.yml configuration template:
```yml
projectPath: /home/tano/workspace_autogenerate/springboot-3layer-generator/demo
# projectPath: c://workspace_private/springboot-3layer-generator/demo
classesDirectory: build/classes/java/main
outputDirectory : src/main/java
options:
  dtoLayer : true # generates the dto from entities, controller using dto, and mapper layer
  serviceInterface: true # whatever to generate CrudService Interface or not
  entityControllers: false # whatever to generate controller using jpa entity or not

inputPackages:
  jpaEntities : com.example.demo.model

outputPackages:
  repositories : com.example.demo.repository
  services: com.example.demo.service
  controllers: com.example.demo.controller
  dtos: gae.piaz.layer3gen.output.dtos # if this in not specified the dto package will be under the controllers package
  mappers: gae.piaz.layer3gen.output.mappers # if this in not specified the mapper package will be under the services package
```

## Examples: 
Checkout the example project with generated classes present in **/demo** and **/demo-jakarta** folder.

### Repository generated example class:
```java
@Repository
public interface BooksRepository extends JpaRepository<Books, java.lang.Integer> {

}
```
### Service generated example class:
```java
@Service
public class BooksService implements CrudService<Books,java.lang.Integer> {

    @Autowired
    private BooksRepository repository;

    @Override
    public Books create(Books entity) {
        return repository.save(entity);
    }

    @Override
    public Books update(Books entity) {
        return repository.save(entity);
    }

    @Override
    public Page<Book> read(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Book> readOne(java.lang.Integer primaryKey) {
        return repository.findById(primaryKey);
    }

    @Override
    public void delete(java.lang.Integer primaryKey) {
        repository.deleteById(primaryKey);
    }
}
```
### Controller generated example class:
```java
@RestController
@RequestMapping("/books-dto/")
public class BooksControllerDTO implements CrudController<BooksDTO,java.lang.Integer>{

    @Autowired
    private BooksService service;

    @Autowired
    private BooksMapper mapper;

    @Override
    public ResponseEntity<BooksDTO> create(@RequestBody BooksDTO dto) {
       Books entity = mapper.toEntity(dto);
       entity = service.create(entity);
       return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<BooksDTO> update(@RequestBody BooksDTO dto) {
      Books entity = mapper.toEntity(dto);
       entity = service.update(entity);
       return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<Page<BookDTO>> read(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<BookDTO> pages = service.read(pageable).map(mapper::toDto);
        return ResponseEntity.ok(pages);
    }

    @Override
    public ResponseEntity<BookDTO> readOne(@PathVariable("id") java.lang.Integer primaryKey) {
        Optional<Book> entity = service.readOne(primaryKey);
        return entity.map(e -> ResponseEntity.ok(mapper.toDto(e))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public void delete(java.lang.Integer primaryKey) {
        service.delete(primaryKey);
    }
}
```

Other insight can be found in this blog post: https://medium.com/p/49f3fbbc7b2d