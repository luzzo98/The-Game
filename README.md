# The Game

The project is a digital and distributed version of "The Game" whose rules can be found [here](https://www.ultraboardgames.com/the-game/game-rules.php).

In short, the game consists in one or more players working together without communicate in order to dispose of the cards in the main deck in which cards numbered 2 to 99 are placed in random order.
The cards must be placed in four cluster of which two start at number 1 and can only hold cards higher than the last played, while the other two start at number 99 and can only hold the smaller ones.

This is only the principle behind the game; to know all the dynamics of the game, you must consult the complete rules.

## Project Information

This project is configured with the Gradle Wrapper, which automates the handling of the required Java version for the build.
The Gradle Wrapper ensures that users don't need to manually install a specific Java version, simplifying the setup process.

Additionally, Checkstyle is integrated into the build process to enforce coding standards and improve code quality.

In order to create a self-contained JAR containing all dependencies, the Shadow plugin was used, which made it possible to overcome the problems of handling Akka by other tools.
In particular, the merging of the default 'reference.conf' file and 'application.conf' with the specific configuration for this project ensured the proper functioning of the Akka cluster.

This project leverages GitLab CI/CD for continuous integration.
The CI/CD pipelines are configured to automate the building and testing; these automatic procedures validate code changes at each push, promoting software stability and quality.
For details on contributing to the CI/CD setup, please refer to the `.gitlab-ci.yml` file in the repository.

## Getting Started

1. Open a command-line terminal and clone the repository:

    ```bash
    git clone https://github.com/luzzo98/The-Game.git

2. Navigate to the project directory:

    ```bash
    cd The-Game

3. Build the project using the Gradle Wrapper and wait for the download of the dependencies and the compilation of the source code (it may take a few minutes):

    ```bash
    ./gradlew build

4. After the build is completed, run the project using the Gradle Wrapper:

    ```bash
    ./gradlew run

## Usage

After the build the project will include a JAR file that can be executed.
Ensure you have Java 17 or a newer version installed on your machine.
To run the JAR, use the following command:

```bash
java -jar .\build\libs\The_Game.jar
```

## Requirements

- The authorization to access the repository.
- Java 5 or higher to be able to use the Gradle wrapper and build the project.
- Java 17 or higher is required for running the JAR independently.

## Contact Information:
For any questions and feedback feel free to contact me.

Simone Luzi - simone.luzi@studio.unibo.it
