Test Automation Framework built using Maven, TestNG and selenium to validate SightLine functionalities. This is a Page Object Model (POM) framework and below details brief about folder structure. 

src/main/java is a root folder under this we have following packages:

automationLibrary - Contains main driver and element classes.
configsAndTestData - Contains class and XML files of configMain, environments and test data.
pageFactory - Each page of the application is mapped to specific class here. All locators and functions of the specific page are maintained in specific class.

<!-- For POC Use testScriptSmoke -->
testScriptSmoke - contains classes where somke test scenarios are covered. We could see sepearte class based on module.
testScriptsRegression- contains classes where regression scenarios are covered.

Other folders at project source level

BrowserDrivers - contains chrome, IE browser drivers which are used by selenium to communicate with respective browsers. This folder also contains bat files to kill background process of the browsers post execution of scripts.

Misc - Gmail properties file to deal with emails validation, fetch an OTP and activation links. Contains 'BatchPrintFiles' folder to manage batch print files. Also contains batch file to validate batch upload functionality.

Screenshots - Store the Failed or Passed screenshots
