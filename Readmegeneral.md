# Sistema de Control de Gastos



Sistema integral y modular diseñado para la gestión, seguimiento y análisis de finanzas personales. El proyecto está construido bajo una estricta separación de responsabilidades, aislando la lógica de negocio y financiera de la interfaz de usuario.



#### Arquitectura del Sistema



El repositorio está organizado en un esquema modular que permite mantener la escalabilidad, la reutilización de código y la mantenibilidad a largo plazo:



```text

ControlGastos/

├── gasto-core/           # Módulo Núcleo: Lógica financiera, modelos y persistencia de datos.

└── AppControlGastos/     # Módulo de Presentación: Aplicación móvil en Android / Kotlin.



#### Arquitectura Modular y Desacoplamiento:



gasto-core: Núcleo independiente del sistema. Contiene los modelos de dominio, las reglas de validación financiera, el control de presupuestos y la gestión de la base de datos local, completamente desacoplado de cualquier plataforma visual.



AppControlGastos: Aplicación cliente orientada al usuario final desarrollada en Android (Kotlin). Consume los servicios del núcleo para renderizar dashboards, formularios de transacciones y pantallas de historial de forma reactiva.



#### Control de Versiones y Gestión de Dependencias:



Se utiliza Gradle como herramienta centralizada de automatización de compilación y gestión de dependencias, asegurando versiones estables y compatibles de Kotlin, librerías de persistencia y componentes de interfaz.



El control de versiones se maneja mediante Git, estructurando ramas independientes para el desarrollo de nuevas funcionalidades (feature/), corrección de errores (fix/) y ramas estables para producción (main / develop).



#### Ciclo de Vida y Pruebas de Calidad:



El flujo de trabajo del proyecto contempla la ejecución de pruebas unitarias automatizadas sobre el módulo gasto-core para garantizar la precisión matemática de los reportes y presupuestos.



Para la parte visual (AppControlGastos), se contemplan pruebas de instrumentación y simulación de flujos de usuario en emuladores y dispositivos físicos con diversas densidades de pantalla y versiones del sistema operativo Android.



#### Tecnologías y Herramientas Principales



Lenguaje Base: Kotlin (versión moderna y segura para nulidad).



Gestión de Dependencias: Gradle (con soporte para compilación modular).



Persistencia Local: Room Database / SQLite / DAOs.



Arquitectura UI: MVVM (Model-View-ViewModel) con flujos reactivos (StateFlow / LiveData).



Control de Versiones: Git.



