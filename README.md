# Dragon rockets
Coding Exercise: Dragon Rockets Repository

## Prerequisites

Before you begin, ensure you have met the following requirements:

* **Java Development Kit (JDK) 21**
* **Gradle 8.14.2**

## Getting Started

Follow these steps to get your development environment set up.

### Cloning the Repository

To get a copy of the project, clone the repository to your local machine:

```bash
git clone https://github.com/Ramir0/dragon-rockets.git
cd dragon-rockets
```

### Building the Project

Once you have cloned the repository, you can build the project using Gradle:

```bash
./gradlew build
```

This command will download all necessary dependencies and compile the project.

### Running Tests

This project includes unit tests and integration tests.

#### Run all unit tests:

```bash
./gradlew test
```

#### Run all integration tests:

```bash
./gradlew integrationTest
```

#### Run all tests (unit and integration):

```bash
./gradlew check
```

### Example Scenario

The example scenario can be validated in `DragonRocketsIntegrationTest.java`

```
Transit – In progress – Dragons: 3
    Red Dragon – On ground
    Dragon XL – In space
    Falcon Heavy – In space
Luna1 – Pending – Dragons: 2
    Dragon 1 – In space
    Dragon 2 – In repair
Vertical Landing – Ended – Dragons: 0
Mars – Scheduled – Dragons: 0
Luna2 – Scheduled – Dragons: 0
Double Landing – Ended – Dragons: 0
```

### Notes

No AI tools were used in development, but I am considering it for Java documentation comments.

#### TO-DO
1. Create a status validator
2. Add Custom Exceptions
3. Implement remove operations
4. Add logs
5. Implement a mechanism to get the missions and rockets history
6. There is always room for improvement
