import { createWriteStream, readFileSync, unlink } from "fs";
import { exec } from "child_process";
import { basename, dirname, join } from "path";
import archiver from "archiver";

const executeWithOutput = (command, postExecute = null) => {
  exec(command, (error, stdout, stderr) => {
    if (error !== null) {
      console.log(`Command failed with error ${error}: ${command}`);
      console.error(stderr);
      console.log(stdout);
    }
    if (postExecute) {
      postExecute();
    }
  });
};

const zipFolderIntoFile = (folderPath) => {
  const basePath = basename(folderPath);
  const archiveFileName = `${basePath}.zip`;
  const output = createWriteStream(archiveFileName);
  const archive = archiver("zip", {
    zlib: { level: 9 }, // Sets the compression level.
  });

  output.on("close", () => {
    console.log(archive.pointer() + " total bytes zipped");
  });

  archive.on("warning", (err) => {
    if (err.code === "ENOENT") {
      console.log("No such file or directory");
    } else {
      throw err;
    }
  });

  archive.on("error", (err) => {
    throw err;
  });

  archive.pipe(output);
  console.log(`${folderPath} -> ${basePath}`);
  archive.directory(folderPath, basePath);
  archive.finalize();

  return archiveFileName;
};

const loadPublishedArduinoLib = (libString) => {
  executeWithOutput(`arduino-cli lib install -v "${libString}"`);
};

const loadLocalFilePathArduinoLib = (libString, operatingDir) => {
  const folderPath = join(operatingDir, libString);
  const zipFileName = zipFolderIntoFile(folderPath);

  /*executeWithOutput(
    `ARDUINO_LIBRARY_ENABLE_UNSAFE_INSTALL=true arduino-cli lib install -v --zip-path "${zipFileName}"`,
    () => {
      unlink(zipFileName, (err) => {
        if (err) {
          throw err;
        }
      });
    }
  );*/
};

const loadArduinoLib = (operatingDir) => (libString) => {
  console.log(`Loading arduino lib: ${libString}`);

  if (libString.includes("@")) {
    loadPublishedArduinoLib(libString);
  } else {
    loadLocalFilePathArduinoLib(libString, operatingDir);
  }
};

const configFile = process.argv.length > 2 ? process.argv[2] : "package.json";

console.log(`Reading config file: ${configFile}`);

const operatingDir = dirname(configFile);
const rawFile = readFileSync(configFile);
const configJson = JSON.parse(rawFile);

configJson.arduinoLibs.forEach(loadArduinoLib(operatingDir));
