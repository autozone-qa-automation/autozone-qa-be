# Autozone QA Automation — Backend

Tecnológico de Monterrey — Campus Chihuahua
Desarrollo e Implantación de Sistemas de Software — TC3005B GPO500
Semestre Febrero – Junio 2025

---

## Stack

- [Java 17](https://openjdk.org/projects/jdk/17/)
- [Spring Boot 4](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL](https://www.mysql.com/)
- [JUnit 5](https://junit.org/junit5/) + [Spring Boot Test](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Spotless](https://github.com/diffplug/spotless) + [Google Java Format](https://github.com/google/google-java-format)

---

## Requisitos previos

- Java 17
- Maven 3.9+ (o usar el wrapper incluido `./mvnw`)
- MySQL corriendo localmente

---

## Configuración local

### 1. Crea la base de datos en MySQL

Conéctate a tu instancia local de MySQL y ejecuta:

```sql
CREATE DATABASE az_qa_local;
```

### 2. Crea el archivo `.env`

En la raíz del proyecto (al mismo nivel que `pom.xml`), crea un archivo llamado `.env` con el siguiente contenido:

```
DB_NAME=az_qa_local
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password
```

> **Nota:** El archivo `.env` está en `.gitignore` y nunca debe subirse al repositorio. Cada desarrollador tiene su propia copia local con sus credenciales.

### 3. Verifica que MySQL esté corriendo

```bash
mysql -u tu_usuario -p -e "SHOW DATABASES;" | grep az_qa_local
```

Si ves `az_qa_local` en el output, estás listo para correr el proyecto.

---

## Cómo correr el proyecto

```bash
# Compilar
./mvnw clean install

# Correr en desarrollo
./mvnw spring-boot:run

# Correr pruebas
./mvnw test

# Correr linter (verificar formato)
./mvnw spotless:check

# Correr linter con autofix
./mvnw spotless:apply

# Build de producción
./mvnw clean package
```

---

## Cómo contribuir

### 1. Asegúrate de estar en `main` y actualizado

```bash
git checkout main
git pull origin main
```

### 2. Crea tu rama con la nomenclatura de Jira

El nombre de la rama debe seguir el formato `AZ-#` donde `#` es el número de ticket:

```bash
git checkout -b AZ-123
```

### 3. Haz tus cambios

Trabaja en tu rama. Antes de hacer commit, verifica que tu código pase el linter y los tests:

```bash
./mvnw spotless:apply   # corregir formato automáticamente
./mvnw spotless:check   # verificar que no haya errores de formato
./mvnw test             # correr pruebas
./mvnw clean package    # verificar que el build no truene
```

### 4. Sube tu rama y abre un Merge Request

```bash
git push origin AZ-123
```

Ve a GitHub, abre un Pull Request desde tu rama hacia `main` y solicita el review de al menos uno de los Git Guardians listados abajo.

---

## Acceptance criteria

Para que un PR pueda ser mergeado a `main` debe cumplir **todos** los siguientes criterios:

- [ ] Aprobación de al menos un Git Guardian (admin o miembro del team `reviewers`)
- [ ] El job de **Lint** pasa en el pipeline de CI (`./mvnw spotless:check` sin errores)
- [ ] Todos los **tests** pasan (`./mvnw test`)
- [ ] El **build** compila sin errores (`./mvnw clean package`)

Estos checks corren automáticamente en GitHub Actions al abrir o actualizar un PR. También puedes verificarlos localmente antes de hacer push.

---

## Git Guardians

Personas autorizadas para aprobar Pull Requests:

| Nombre | Matrícula | GitHub |
|---|---|---|
| Alonso Alarcón | A01563388 | @alxxjandro |
| Alejandro Carrillo | A01567228 | @Carrillo-coder |
| Rocío Rodríguez | A01563530 | @RocioElysaRodriguez |
| Saúl Campos | A01567242 | @saulito-tec |
| Eliel Mejía | A01563697 | @elimeji |

---

## Configurar Format on Save en IntelliJ IDEA

### Plugin requerido

Instala el plugin **google-java-format** en IntelliJ:

1. Ve a **Settings → Plugins → Marketplace**
2. Busca `google-java-format` e instálalo
3. Reinicia IntelliJ

### Activar google-java-format

1. Ve a **Settings → google-java-format Settings**
2. Activa **Enable google-java-format**
3. Selecciona el estilo **Google**

### Activar Format on Save

1. Ve a **Settings → Tools → Actions on Save**
2. Activa **Reformat code**
3. Activa **Optimize imports**

Con esto, cada vez que guardes un archivo IntelliJ aplica Google Java Format automáticamente.

---

## Configurar Format on Save en VS Code

### Extensiones requeridas

Instala las siguientes extensiones en VS Code:

- [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)

### Activar Format on Save

1. Abre Settings con `Ctrl + ,` (o `Cmd + ,` en Mac)
2. Busca `Format on Save`
3. Activa la casilla **Editor: Format On Save**

### Verificar que Format on Save está activo en el proyecto

El repositorio incluye un archivo `.claude/settings.json` con un hook que ejecuta `./mvnw spotless:apply` automáticamente al guardar cualquier archivo `.java` desde Claude Code. Para VS Code nativo, asegúrate de que tu `settings.json` tenga:

```json
{
  "editor.formatOnSave": true,
  "[java]": {
    "editor.defaultFormatter": "redhat.java"
  }
}
```