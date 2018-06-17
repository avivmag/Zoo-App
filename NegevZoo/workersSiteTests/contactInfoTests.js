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
    startContactInfos();
    startFailContactInfoNoVia();
    startFailContactInfoNoAddress();
    startSuccessContactInfo();
    startDeleteContactInfo();
    startAddContactInfoNote();
    finishTests();
}

function startContactInfos() {
    driver.findElement(By.name('generalInfo')).sendKeys(webdriver.Key.ENTER);
    
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('contact-info')).sendKeys(webdriver.Key.ENTER);

        driver.sleep(2500);
    });
}

function startFailContactInfoNoVia() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('address')).sendKeys('new address');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא הכנס דרך התקשרות') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-contact-info-no-via', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-contact-info-no-via', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-contact-info-no-via', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startFailContactInfoNoAddress() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('address')).clear();
        driver.findElement(By.name('via')).sendKeys('new via');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא הכנס כתובת התקשרות') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-contact-info-no-address', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-contact-info-no-address', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-contact-info-no-address', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startSuccessContactInfo() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('address')).clear();
        driver.findElement(By.name('via')).clear();
        driver.findElement(By.name('address')).sendKeys('new address');
        driver.findElement(By.name('via')).sendKeys('new via');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'דרך ההתקשרות נוספה בהצלחה!') {
                        output = 'passed';
    
                        testResults.push({testName: 'success-contact-info', testOutput: output});

                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-contact-info', testOutput: output});
                        
                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-contact-info', testOutput: output});

                    driver.findElement(By.className('md-confirm-button')).click();
                });
          });
}

function startAddContactInfoNote() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('contact-info-note')).sendKeys('new contact info note');
        driver.findElement(By.name('save-note')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'התוכן נשמר בהצלחה') {
                        output = 'passed';
    
                        testResults.push({testName: 'success-contact-info-note', testOutput: output});

                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-contact-info-note', testOutput: output});
                        
                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-contact-info-note', testOutput: output});

                    driver.findElement(By.className('md-confirm-button')).click();
                });
          });
}

function startDeleteContactInfo() {
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
                            if (text === 'דרך יצירת הקשר נמחקה בהצלחה') {
                                output = 'passed';
            
                                testResults.push({testName: 'delete-contact-info', testOutput: output});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                            else {
                                output = 'failed - alert shown was incorrect: ' + text;
            
                                testResults.push({testName: 'delete-contact-info', testOutput: output});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                        },
                        function () {
                            output = 'failed - no alert was shown.';
            
                            testResults.push({testName: 'delete-contact-info', testOutput: output});
                            
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
