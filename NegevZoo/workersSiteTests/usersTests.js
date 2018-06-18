var webdriver = require('selenium-webdriver'),
    By = webdriver.By,
    until = webdriver.until;

var driver = new webdriver.Builder()
    .forBrowser('firefox')
    .build();

let testResults = [];

driver.get('http://negevzoo.sytes.net:50555');

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
    startUsers();
    startFailNewUserNoUsername();
    startFailNewUserNoPassword();
    startFailPasswordLessThan6();
    startSuccessNewUser();
    startFailExistingUsername();
    startResetPassword();
    startDeleteUser();
    finishTests();
}

function startUsers() {
    driver.findElement(By.name('users')).sendKeys(webdriver.Key.ENTER);

    driver.sleep(2500);
}

function startFailNewUserNoUsername() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('password-save')).sendKeys('new password');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר שם משתמש') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-user-no-username', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-user-no-username', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-user-no-username', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startFailNewUserNoPassword() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('password-save')).clear();
        driver.findElement(By.name('username-save')).sendKeys('new username');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא בחר סיסמא') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-user-no-password', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-user-no-password', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-user-no-password', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startFailPasswordLessThan6() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('password-save')).clear();
        driver.findElement(By.name('username-save')).clear();
        driver.findElement(By.name('username-save')).sendKeys('new username');
        driver.findElement(By.name('password-save')).sendKeys('short');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'הסיסמא חייבת להיות לפחות 6 תווים') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-user-short-password', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-user-short-password', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-user-short-password', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startSuccessNewUser() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('username-save')).clear();
        driver.findElement(By.name('password-save')).clear();
        driver.findElement(By.name('username-save')).sendKeys('new username');
        driver.findElement(By.name('password-save')).sendKeys('newPassword');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'המשתמש נוסף בהצלחה!') {
                        output = 'passed';
    
                        testResults.push({testName: 'success-user', testOutput: output});

                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-user', testOutput: output});
                        
                        driver.findElement(By.className('md-confirm-button')).click();
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-user', testOutput: output});

                    driver.findElement(By.className('md-confirm-button')).click();
                });
          });
}

function startFailExistingUsername() {
    driver.sleep(1500).then(function () {
        driver.findElement(By.name('password-save')).clear();
        driver.findElement(By.name('username-save')).clear();
        driver.findElement(By.name('username-save')).sendKeys('new username');
        driver.findElement(By.name('password-save')).sendKeys('newPassword');
        driver.findElement(By.name('save')).sendKeys(webdriver.Key.ENTER);
    });
    
    var output = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'שם המשתמש הנבחר כבר בשימוש') {
                        output = 'passed';
    
                        testResults.push({testName: 'failed-user-existing-username', testOutput: output});
                    }
                    else {
                        output = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-user-existing-username', testOutput: output});
                    }
                },
                function () {
                    output = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-user-existing-username', testOutput: output});
                });
    
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
        });
}

function startResetPassword() {
    var deleteButton = driver.findElement(By.name('reset-last'));
    
    driver.executeScript("arguments[0].scrollIntoView()", deleteButton);

    deleteButton.sendKeys(webdriver.Key.ENTER);

    let output = '';
    driver.sleep(500).then(
        function() {
            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);

            driver.sleep(1500).then(
                function () {
                    driver.switchTo().activeElement().sendKeys('newPassword');
                    driver.switchTo().activeElement().sendKeys(webdriver.Key.ENTER);

            driver.sleep(4500).then(
                function() {
                    driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                        function (text) {
                            if (text === 'הסיסמא אופסה בהצלחה') {
                                output = 'passed';
            
                                testResults.push({testName: 'user-reset-password', testOutput: output});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                            else {
                                output = 'failed - alert shown was incorrect: ' + text;
            
                                testResults.push({testName: 'user-reset-password', testOutput: output});

                                driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                            }
                        },
                        function () {
                            output = 'failed - no alert was shown.';
            
                            testResults.push({testName: 'user-reset-password', testOutput: output});
                            
                            driver.findElement(By.className('md-confirm-button')).sendKeys(webdriver.Key.ENTER);
                        });
                });
            });
        });
}

function startDeleteUser() {
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
                            if (text === 'המשתמש נמחק בהצלחה') {
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
