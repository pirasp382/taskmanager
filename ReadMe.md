## Projektbeschreibung
Das Projekt "TaskManager" ist eine Anwendung zur Verwaltung von Aufgaben.
Es ermöglicht Benutzern, Aufgaben zu erstellen, zu bearbeiten und zu löschen,
um ihre Produktivität zu steigern. Die Anwendung bietet eine 
benutzerfreundliche Oberfläche und ist darauf ausgelegt, eine einfache und 
effiziente Verwaltung von Aufgaben zu ermöglichen.

### Techstack
- Frontend
  - React.js
  - CSS
  - Axios
- Backend
  - Quarkus
  - Hibernate
  - MariaDb

## Installation
klone das Repository
```bash
git clone https://github.com/pirasp382/taskmanager.git
```
### Backend
```bash
cd taskmanager/taskmanager_backend;
./mvwn quarkus:dev
```

### Frontend
```bash
cd taskmanager/taskmanager_frontend;
npm install;
npm start;
```

## Usage

- Rufen Sie die Anwendung unter `http://localhost:3000` für das Frontend und
 `http://localhost:8080` für das Backend auf.


## License

This project is licensed under the MIT License.