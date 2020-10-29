# Gradle plugin - layer3gen
Generates the standard SPRING layered CRUD architecture starting from JPA entities.

1) repositories
2) services
3) controller

## How to:
##### 1- configure build.gradle as follow:
```groovy

buildscript {
	dependencies {
		classpath "gradle.plugin.gae.piaz:layer3gen:1.4"
	}
	// ....
}
  // ...
  // ...
apply plugin: 'gae.piaz.layer3gen'

```
##### 2- Create the file _3layer-settings.yml_ in src/main/resources/ folder.
Example:
```yml
projectPath: /home/tano/workspace_autogenerate/springboot-3layer-generator
outputDirectory : /src/main/java
options:
    dtoLayer : true

inputPackages:
    jpaEntities : com.gae.piaz.autogen.model

outputPackages:
    repositories : com.gae.piaz.autogen.repositorygen
    services: com.gae.piaz.autogen.servicegen
    controllers: com.gae.piaz.autogen.controllergen
  
```
##### 3- run 
```shell script
gradlew layer3gen
```

## Examples: 
Checkout the example project with generated classes present in **/demo** folder.

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
    public Page<Books> read(Books entity, Pageable pageable) {
        Example<Books> example = Example.of(entity);
        return repository.findAll(example,pageable);
    }

    @Override
    public Books readOne(java.lang.Integer primaryKey) {
        return repository.getOne(primaryKey);
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
    public ResponseEntity<Page<BooksDTO>> read(
            @RequestBody BooksDTO dto,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Books entity = mapper.toEntity(dto);
        Page<BooksDTO> pages = service.read(entity, pageable).map(mapper::toDto);
        return ResponseEntity.ok(pages);
    }

    @Override
    public ResponseEntity<BooksDTO> readOne(@PathVariable("id") java.lang.Integer primaryKey) {
         Books entity = service.readOne(primaryKey);
         return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public void delete(java.lang.Integer primaryKey) {
        service.delete(primaryKey);
    }
}
```


