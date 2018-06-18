var webdriver = require('selenium-webdriver'),
    By = webdriver.By,
    until = webdriver.until;

var driver = new webdriver.Builder()
    .forBrowser('firefox')
    .build();

let testResults = [];

driver.get('http://negevzoo.sytes.net:8080');

driver.sleep(1000).then(function () {
    startSuccessLogin();
});

function startSuccessLogin() {
    driver.findElement(By.name('username')).sendKeys('or');

    driver.sleep(500).then(function () {
        driver.findElement(By.name('password')).sendKeys('drdrdr');
    });
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('login')).click();
    });
    
    var successLoginOutput  = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    successLoginOutput = 'failed - alert was shown: ' + text;
    
                    testResults.push({testName: 'success-login', testOutput: successLoginOutput});

                    finishTests();
                },
                function () { });
                
            driver.getTitle().then(function(title) {
                if (title === 'Feed Wall') {
                    successLoginOutput = 'passed';
    
                    testResults.push({testName: 'success-login', testOutput: successLoginOutput});
                    
                    startTests();
                }
                else {
                    testResults.push({testName: 'loginTimeout', testOutput: 'login timeout.'});
    
                    finishTests();
                }
            });
        });
}
    
function startTests() {
    startNewEnclosure();
    startSuccessEnclosure();
}

function startAnimalTests() {
    startNewAnimal();

    driver.sleep(1000).then(function () {
        startFailAnimalName();
        startFailAnimalPreservation();
        startSuccessAnimal();
    });
}

function startNewAnimalTests() {
    startSuccessAnimalDetail();
    startDeleteAnimal();
    finishTests();
}

function startNewEnclosure() {
    driver.findElement(By.name('enclosures')).sendKeys(webdriver.Key.ENTER);

    driver.sleep(1500).then(function () {
            driver.findElement(By.name('new-enclosure')).click();
    });
}

function startSuccessEnclosure() {
    driver.findElement(By.name('enclosure-name')).sendKeys('new enclosure');
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('save')).click();
    });
    
    var successEnclosureOutput = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'המתחם נוסף בהצלחה!') {
                        successEnclosureOutput = 'passed';
    
                        testResults.push({testName: 'success-enclosure', testOutput: successEnclosureOutput});

                        driver.findElement(By.className('md-confirm-button')).click();

                        startAnimalTests();
                    }
                    else {
                        successEnclosureOutput = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-enclosure', testOutput: successEnclosureOutput});
                        
                        driver.findElement(By.className('md-confirm-button')).click();

                        finishTests();
                    }
                },
                function () {
                    successEnclosureOutput = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-enclosure', testOutput: successEnclosureOutput});

                    driver.findElement(By.className('md-confirm-button')).click();

                    finishTests();
                });
          });
}

function startNewAnimal() {
    driver.findElement(By.name('new-animal')).sendKeys(webdriver.Key.ENTER);
}

function startFailAnimalName() {
    driver.findElement(By.name("animal-save")).click();

    var failedEnclosureNameOutput = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                function (text) {
                    if (text === 'אנא בחר שם לחיה') {
                        failedEnclosureNameOutput = 'passed';
    
                        testResults.push({testName: 'failed-animal-name', testOutput: failedEnclosureNameOutput});
                    }
                    else {
                        failedEnclosureNameOutput = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-animal-name', testOutput: failedEnclosureNameOutput});
                    }
                },
                function () {
                    failedEnclosureNameOutput = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-animal-name', testOutput: failedEnclosureNameOutput});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();
        });
}

function startFailAnimalPreservation() {
    driver.findElement(By.name('animal-name')).sendKeys('new animal');
    driver.findElement(By.name('animal-save')).sendKeys(webdriver.Key.ENTER);

    var failedEnclosureNameOutput = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                function (text) {
                    if (text === 'אנא בחר רמת שימור לחיה') {
                        failedEnclosureNameOutput = 'passed';
    
                        testResults.push({testName: 'failed-animal-preservation', testOutput: failedEnclosureNameOutput});
                    }
                    else {
                        failedEnclosureNameOutput = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-animal-preservation', testOutput: failedEnclosureNameOutput});
                    }
                },
                function () {
                    failedEnclosureNameOutput = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-animal-preservation', testOutput: failedEnclosureNameOutput});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();
        });
}

function startSuccessAnimal() {
    driver.findElement(By.name('animal-name')).clear();
    driver.findElement(By.name('animal-name')).sendKeys('new animal');
    driver.findElement(By.name('preservation-select')).sendKeys(webdriver.Key.ENTER);
    driver.findElement(By.name('p0')).sendKeys(webdriver.Key.ENTER);
    driver.findElement(By.name('p0')).sendKeys(webdriver.Key.ESCAPE);

    driver.sleep(500).then(function () {
          driver.findElement(By.name('animal-save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var successEnclosureOutput = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'החיה נוספה בהצלחה!') {
                        successEnclosureOutput = 'passed';
    
                        testResults.push({testName: 'success-animal', testOutput: successEnclosureOutput});

                        driver.findElement(By.className('md-confirm-button')).click();

                        startNewAnimalTests();
                    }
                    else {
                        successEnclosureOutput = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-animal', testOutput: successEnclosureOutput});
                        
                        driver.findElement(By.className('md-confirm-button')).click();

                        finishTests();
                    }
                },
                function () {
                    successEnclosureOutput = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-animal', testOutput: successEnclosureOutput});

                    driver.findElement(By.className('md-confirm-button')).click();

                    finishTests();
                });
          });
}

function startSuccessAnimalDetail() {
    var detailName = driver.findElement(By.name('detail-name0'));
    
    driver.executeScript("arguments[0].scrollIntoView()", detailName);

    detailName.sendKeys('חיה חדשה');

    driver.findElement(By.name('add-detail')).sendKeys(webdriver.Key.ENTER);

    var output = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'החיה עודכנה בהצלחה!') {
                        output = 'passed';
    
                        testResults.push({testName: 'success-animal-detail', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-animal-detail', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-animal-detail', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
          });
}

function startDeleteAnimal() {
    driver.sleep(1500).then(function() {
        var deleteAnimal = driver.findElement(By.name('animal-delete'));
        
        driver.executeScript("arguments[0].scrollIntoView()", deleteAnimal);
    
        deleteAnimal.sendKeys(webdriver.Key.ENTER);
    
        let output = '';
        driver.sleep(500).then(
            function() {
                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
    
                driver.sleep(4500).then(
                    function() {
                        driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                            function (text) {
                                if (text === 'החיה נמחקה בהצלחה!') {
                                    output = 'passed';
                
                                    testResults.push({testName: 'delete-animal', testOutput: output});
    
                                    driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);

                                    driver.sleep(2500).then(function () {
                                        startDeleteEnclosure(false);
                                    });
                                }
                                else {
                                    output = 'failed - alert shown was incorrect: ' + text;
                
                                    testResults.push({testName: 'delete-animal', testOutput: output});
    
                                    driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);

                                    driver.sleep(500).then(function () {
                                        startDeleteEnclosure(true);
                                    });
                                }
                            },
                            function () {
                                output = 'failed - no alert was shown.';
                
                                testResults.push({testName: 'delete-animal', testOutput: output});
    
                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);

                                driver.sleep(500).then(function () {
                                    startDeleteEnclosure(true);
                                });
                            });
                    })
              });
    });
}

function startDeleteEnclosure(shouldReDirect) {
    if (shouldReDirect) {
        driver.findElement(By.name('back-enclosure')).sendKeys(webdriver.Key.ENTER);
    }

    driver.sleep(1500).then(function() {
        var deleteEnclosure = driver.findElement(By.name('delete'));
        
        driver.executeScript("arguments[0].scrollIntoView()", deleteEnclosure);
    
        deleteEnclosure.sendKeys(webdriver.Key.ENTER);
    
        let deleteEnclosureOutput = '';
        driver.sleep(500).then(
            function() {
                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
    
                driver.sleep(4500).then(
                    function() {
                        driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                            function (text) {
                                if (text === 'המתחם נמחק בהצלחה!') {
                                    deleteEnclosureOutput = 'passed';
                
                                    testResults.push({testName: 'delete-enclosure', testOutput: deleteEnclosureOutput});
    
                                    driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                                }
                                else {
                                    deleteEnclosureOutput = 'failed - alert shown was incorrect: ' + text;
                
                                    testResults.push({testName: 'delete-enclosure', testOutput: deleteEnclosureOutput});
    
                                    driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                                }
                            },
                            function () {
                                deleteEnclosureOutput = 'failed - no alert was shown.';
                
                                testResults.push({testName: 'delete-enclosure', testOutput: deleteEnclosureOutput});
    
                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            });
                    })
              });
    });
}

function finishTests() {
    
    driver.sleep(1000).then(function() {
          console.log(testResults);
          // Send log to server.
    });
    
    driver.quit();
}
