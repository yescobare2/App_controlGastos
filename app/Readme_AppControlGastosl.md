# AppControlGastos (Aplicación Móvil - UI y Presentación)

Este módulo representa la capa de cliente móvil desarrollada en Android con Kotlin. Su única responsabilidad es gestionar la interacción visual, capturar los datos ingresados por el usuario y presentar de forma intuitiva los resultados calculados por gasto-core.

#### 

#### Dashboard y Vista General (/ui/dashboard):



Pantalla principal que actúa como el centro de control financiero del usuario.



Muestra tarjetas con el balance actual en tiempo real, el total de ingresos y el total de egresos del mes en curso.



Integra componentes gráficos (como gráficos de pastel o barras) que muestran el desglose rápido de los gastos por categoría gracias a los datos procesados en el núcleo.



#### Formulario y Registro de Transacciones (/ui/transaction):



Interfaz interactiva para capturar nuevos ingresos o egresos de manera rápida y eficiente.



Incluye campos numéricos optimizados para montos, selectores desplegables de categorías vinculadas, selectores de fecha/hora y campos de texto para notas descriptivas.



Valida en tiempo real la entrada del usuario (por ejemplo, evitando campos vacíos o montos con formatos incorrectos) antes de enviar la orden de guardado al módulo core.

#### 

#### Historial y Filtrado Avanzado (/ui/history):



Listados dinámicos optimizados (mediante adaptadores modernos o LazyLists en Jetpack Compose) para mostrar grandes volúmenes de transacciones sin afectar el rendimiento de la app.



Barra de herramientas con opciones de filtrado interactivo para ordenar los movimientos por fecha (de más reciente a más antiguo), por tipo de transacción o por categorías específicas.



Opciones de gestos (deslizar para eliminar o editar) para el mantenimiento rápido de los registros históricos.



#### ViewModels y Gestión de Estado (/ui/viewmodel):



Implementan el patrón arquitectónico MVVM (Model-View-ViewModel).



Retienen y gestionan el estado de la interfaz de usuario de forma segura ante cambios de configuración (como rotaciones de pantalla de la app).



Actúan como puente reactivo entre las vistas (Activities, Fragments o pantallas de Compose) y los servicios lógicos de gasto-core, utilizando tecnologías como StateFlow o LiveData para actualizar la interfaz automáticamente cuando los datos cambian.

