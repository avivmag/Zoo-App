var webdriver = require('selenium-webdriver'),
    By = webdriver.By,
    until = webdriver.until;

var driver = new webdriver.Builder()
    .forBrowser('firefox')
    .build();

let testResults = [];

driver.get('http://localhost:4646');

startSuccessLogin();

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
    startSpecialEvents();
    startFailSpecialEventNoTitle();
    startFailSpecialEventNoDescription();
    startSuccessSpecialEvent();
    startDeleteSpecialEvent();
    finishTests();
}

function startSpecialEvents() {
    driver.findElement(By.name('specialEvents')).sendKeys(webdriver.Key.ENTER);

    driver.sleep(2500);
}

function startFailSpecialEventNoTitle() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('description-save')).sendKeys('new description');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר כותרת לאירוע') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-special-event-no-title', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-special-event-no-title', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-special-event-no-title', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startFailSpecialEventNoDescription() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('description-save')).clear();
        driver.findElement(By.name('title-save')).sendKeys('new title');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר תיאור לאירוע') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-special-event-no-description', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-special-event-no-description', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-special-event-no-description', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startSuccessSpecialEvent() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('title-save')).clear();
        driver.findElement(By.name('description-save')).clear();
        driver.findElement(By.name('title-save')).sendKeys('new title');
        driver.findElement(By.name('description-save')).sendKeys('new description');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'האירוע נוסף בהצלחה!') {
                        output = 'passed';
    
                        testResults.push({testName: 'success-special-event', testOutput: output});

                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-special-event', testOutput: output});
                        
                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-special-event', testOutput: output});

                    driver.findElement(By.className('md-confirm-button')).click();
                });
          });
}

function startDeleteSpecialEvent() {
    var deleteButton = driver.findElement(By.name('delete-last'))
    
    driver.executeScript("arguments[0].scrollIntoView()", deleteButton);

    deleteButton.sendKeys(webdriver.Key.ENTER);

    let output = '';
    driver.sleep(500).then(
        function() {
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);

            driver.sleep(4500).then(
                function() {
                    driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                        function (text) {
                            if (text === 'האירוע נמחק בהצלחה') {
                                output = 'passed';
            
                                testResults.push({testName: 'delete-user', testOutput: output});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                            else {
                                output = 'failed - alert shown was incorrect: ' + text;
            
                                testResults.push({testName: 'delete-user', testOutput: output});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                        },
                        function () {
                            output = 'failed - no alert was shown.';
            
                            testResults.push({testName: 'delete-user', testOutput: output});
                            
                            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                        });
                })
          });
}

function finishTests() {
    
    driver.sleep(1000).then(function() {
          console.log(testResults);
          // Send log to server.
    });
    
    driver.quit();
}
