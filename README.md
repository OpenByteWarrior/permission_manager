# Microservicio de Gestión de Permisos

Este microservicio proporciona funcionalidades para gestionar permisos, grupos de permisos, 
roles y componentes, con un enfoque en una gestión centralizada de roles. Está desarrollado utilizando Java y Spring Boot, 
siguiendo una arquitectura limpia y modular para garantizar flexibilidad y escalabilidad.

---

## 🚀 Tecnologías y Librerías

- **Java 17**: Lenguaje principal.
- **Spring Boot**: Framework para simplificar el desarrollo de aplicaciones.
- **ModelMapper**: Mapeo entre objetos de dominio y DTOs.
- **Lombok**: Reducción de código boilerplate con anotaciones.
- **PostgreSQL**: Base de datos relacional para almacenar usuarios y permisos.

---

## ✨ Funcionalidades Principales

1. **Gestión de Permisos**
   
  - 🛠️ CRUD de permisos individuales.
  - 📋 Gestión de grupos de permisos (creación, asignación, y eliminación).

2. **Gestión de Roles**

   - 🛠️ CRUD de roles individuales.
  - 📋 Gestión de grupos de permisos (creación, asignación, y eliminación).

---

## 🗂️ Estructura del Proyecto

```plaintext
└── 📁src
    └── 📁main
        └── 📁java
            └── 📁com
                └── 📁permission_management
                    └── 📁application
                        └── 📁dto
                            └── 📁common
                                └── GroupPermissionDTO.java
                                └── ModuleComponentDTO.java
                                └── PermissionDTO.java
                                └── RoleDTO.java
                            └── 📁request
                                └── RequestAssignAndRemoveBodyDTO.java
                                └── RequestGroupPermissionBodyDTO.java
                                └── RequestRoleBodyDTO.java
                            └── 📁response
                                └── ResponseHttpDTO.java
                        └── 📁service
                            └── CrudService.java
                            └── ResourceAssignmentService.java
                        └── 📁usecase
                            └── GroupPermissionUseCase.java
                            └── ModuleComponentUseCase.java
                            └── PermissionUseCase.java
                            └── RoleUseCase.java
                    └── 📁config
                        └── ModelMapperConfig.java
                    └── 📁domain
                        └── 📁models
                            └── Gateway.java
                            └── GroupPermissionGateway.java
                            └── ModuleComponentGateway.java
                            └── PermissionGateway.java
                            └── Resource.java
                            └── ResourceContainer.java
                            └── RoleGateway.java
                    └── 📁infrastructure
                        └── 📁api
                            └── GroupPermissionRestController.java
                            └── HomeRestController.java
                            └── ModuleComponentRestController.java
                            └── PermissionRestController.java
                            └── RoleRestController.java
                        └── 📁persistence
                            └── 📁adapter
                                └── GroupAdapter.java
                                └── ModuleComponentAdapter.java
                                └── PermissionAdapter.java
                                └── RoleAdapter.java
                            └── 📁entity
                                └── GroupPermission.java
                                └── ModuleComponent.java
                                └── Permission.java
                                └── Role.java
                            └── 📁repository
                                └── GroupPermissionRepository.java
                                └── ModuleComponentRepository.java
                                └── PermissionRepository.java
                                └── RoleRepository.java
                    └── PermissionManagementApplication.java
        └── 📁resources
            └── application-test.yaml
            └── application.yaml
            └── banner.txt
    └── 📁test
        └── 📁java
            └── 📁com
                └── 📁permission_management
                    └── 📁application
                        └── 📁service
                            └── ResourceAssingmentServiceTest.java
                        └── 📁usecase
                            └── GroupPermissionUseCaseTest.java
                            └── ModuleComponentUseCaseTest.java
                            └── PermissionUseCaseTest.java
                            └── RoleUseCaseTest.java
                    └── 📁infrastructure
                        └── 📁api
                        └── 📁persistence
                            └── 📁adapter
                                └── GroupAdapterTest.java
                                └── ModuleComponentAdapterTest.java
                                └── PermissionAdapterTest.java
                                └── RoleAdapterTest.java
```

El proyecto sigue una arquitectura **hexagonal**:

- **`application`**:
  - *`dto`*: Contiene los objetos de transferencia de datos (Data Transfer Objects) que se utilizan para enviar y recibir información entre las diferentes capas del microservicio.
    - *`common`*: Contiene DTOs comunes que se comparten entre múltiples funcionalidades.
    - *`request`*: Contiene DTOs específicos para las solicitudes realizadas hacia el microservicio.
    - *`response`*: Contiene DTOs específicos para las respuestas enviadas desde el microservicio.
  - *`service`*: Contiene servicios auxiliares que no forman parte directa de la lógica de negocio, pero son esenciales para la operación del microservicio.
  - *`usecase`*: Contiene la lógica de negocio que define y ejecuta las acciones centrales del dominio del microservicio.

- **`infrastructure`**:
  - *`api`*: Contiene los controladores REST que exponen los endpoints del microservicio.
  - *`persistence`*: Contiene los elementos relacionados con la capa de persistencia y acceso a la base de datos.
    - *`adapter`*: Contiene las clases que actúan como adaptadores entre los casos de uso (lógica de negocio) y la capa de persistencia, implementando las interfaces definidas en el dominio.
    - *`entity`*: Contiene las clases que representan las tablas de la base de datos (entidades JPA).
    - *`repository`*: Contiene las interfaces y adaptadores que gestionan la interacción con la base de datos mediante JPA.

- **`config`**: Contiene los beans y las configuraciones necesarias para las librerías utilizadas en el microservicio, como Spring Security, ModelMapper, etc.
- 
---

## 🔧 Requisitos Previos

- **Java 17** o superior.
- **Gradle** como herramienta de construcción.
- **PostgreSQL** instalado y configurado.

---

## ⚙️ Configuración del Entorno

1. **Clonar el repositorio**:

   ```bash
   git clone https://github.com/OpenByteWarrior/permission_manager.git
   cd permission_manager
   ```

2. **Configurar las variables de entorno**:
   Crear un archivo `.env` en la raíz del proyecto con las siguientes variables:

   ```env
   DB_URL=jdbc:postgresql://localhost:5432/nombre_base_datos
   DB_USERNAME=usuario
   DB_PASSWORD=contraseña
   ```

   ### Ejemplo explicativo:

   - `DB_URL`: URL de conexión a la base de datos PostgreSQL. Por ejemplo:
     ```env
     DB_URL=jdbc:postgresql://localhost:5432/mi_base_de_datos
     ```
     Cambia `mi_base_de_datos` por el nombre de tu base de datos.

   - `DB_USERNAME`: Nombre de usuario para conectarse a la base de datos. Ejemplo:
     ```env
     DB_USERNAME=admin
     ```

   - `DB_PASSWORD`: Contraseña asociada al usuario de la base de datos. Ejemplo:
     ```env
     DB_PASSWORD=admin123
     ```

3. **Construir y ejecutar**:

   ```bash
   ./gradlew bootRun
   ./gradlew build
   ```

---

## 📋 Endpoints Principales

### 📋 Descargar Configuración Insomnia

  [`insomnia.json`](./src/main/java/com/permission_management/insomnia.json)

### 📋 Gestión de Permisos

- **POST** `/api/permissions`: Crear un nuevo permiso.
- **GET** `/api/permissions`: Obtener todos los permisos.
- **GET** `/api/permissions/{id}` Obtener permiso por id.
- **PUT** `/api/permissions/{id}`: Actualizar un permiso existente.
- **DELETE** `/api/permissions/{id}`: Eliminar un permiso.

### 👥 Gestión de Grupos de Permisos

- **POST** `/api/group_permissions`: Crear un grupo de permisos.
- **GET** `/api/group_permissions`: Obtener todos los grupos de permisos.
- **GET** `/api/group_permissions/{id}`: Obtener grupo de permisos por id.
- **PUT** `/api/group_permissions/{id}`: Actualizar un grupo de permisos.
- **DELETE** `/api/group_permissions/{id}`: Eliminar un grupo de permisos.

### 👥 Gestión de Roles

- **POST** `/api/roles`: Crear un rol.
- **GET** `/api/roles`: Obtener todos los roles.
- **GET** `/api/roles/{id}`: Obtener rol por id.
- **PUT** `/api/roles/{id}`: Actualizar un rol.
- **DELETE** `/api/roles/{id}`: Eliminar un rol.
-   
### 👥 Gestión de Componentes

- **POST** `/api/components`: Crear un componente.
- **GET** `/api/components`: Obtener todos los componentes.
- **GET** `/api/components/{id}`: Obtener componente por id.
- **PUT** `/api/components/{id}`: Actualizar un componente.
- **DELETE** `/api/components/{id}`: Eliminar un componente.  

---

## 🧪 Tests

Para ejecutar los tests unitarios y de integración

 ```bash
   ./gradlew test
 ```

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor, abre un issue o un pull request para cualquier mejora o corrección.
