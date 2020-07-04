I. Información:

	- Componente que permite enviar mensaje de whatsapp de manera masiva.
	- Incluye chromedriver para versión de Google Chrome 81.0.4044.138 en Windows, Mac y Linux.

II. Requisitos:

	- Tener instalado java.

	- Tener configurado la variable de entorno JAVA_HOME con: ..\Java\jre\bin

	- Tener chromedriver para su versión de Google Chrome en la carpeta chromedriver. Pagina de descarga: https://chromedriver.chromium.org/downloads.

	- Configurar el archivo application.yml:
	
		pathDriver: Archivo chromedriver.
		numbers: 	Numeros con codigo de pais y separados por coma ','.
		message: 	Mensaje a enviar.


III. Pasos para ejecutar.

	- Ejecutar el archivo execute como administrador.
	- Al cargar el explorador, pedira que se escanee el QR con su celular.