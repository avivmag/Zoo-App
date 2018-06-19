var webdriver   = require('selenium-webdriver'),
    By          = webdriver.By,
    until       = webdriver.until;

var driver      = new webdriver.Builder()
    .forBrowser('firefox')
    .build();

let testResults = [];

driver.get('http://negevzoo.sytes.net:8080');

driver.sleep(1000).then(function () {
    startSuccessLogin();
});

function startTests() {
    startFailFeedWallTitle();
    startFailFeedWallInfo();
    startSuccessFeedWall();
    startExistingFeedWall();
    startDeleteFeedWall();
    finishTests();
}

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

function startFailFeedWallTitle() {
    driver.findElement(By.name('info-save')).sendKeys('new info');
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('save')).click();
    });
    
    var failedFeedWallTitleOutput = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא הכנס כותרת לעדכון') {
                        failedFeedWallTitleOutput = 'passed';
    
                        testResults.push({testName: 'failed-feedwall-title', testOutput: failedFeedWallTitleOutput});
                    }
                    else {
                        failedFeedWallTitleOutput = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-feedwall-title', testOutput: failedFeedWallTitleOutput});
                    }
                },
                function () {
                    failedFeedWallTitleOutput = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-feedwall-title', testOutput: failedFeedWallTitleOutput});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();
          });
}

function startFailFeedWallInfo() {
    driver.findElement(By.name('info-save')).clear();
    driver.findElement(By.name('title-save')).sendKeys('new title');
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('save')).click();
    });
    
    var failedFeedWallInfoOutput = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'אנא הכנס תוכן לעדכון') {
                        failedFeedWallInfoOutput = 'passed';
    
                        testResults.push({testName: 'failed-feedwall-info', testOutput: failedFeedWallInfoOutput});
                    }
                    else {
                        failedFeedWallInfoOutput = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'failed-feedwall-info', testOutput: failedFeedWallInfoOutput});
                    }
                },
                function () {
                    failedFeedWallInfoOutput = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'failed-feedwall-info', testOutput: failedFeedWallInfoOutput});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();
          });
}

function startSuccessFeedWall() {
    driver.findElement(By.name('info-save')).clear();
    driver.findElement(By.name('title-save')).clear();
    driver.findElement(By.name('title-save')).sendKeys('new title');
    driver.findElement(By.name('info-save')).sendKeys('new info');
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('save')).click();
    });
    
    var successFeedWallOutput = '';
    
    driver.sleep(5000).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'האירוע נוסף בהצלחה!') {
                        successFeedWallOutput = 'passed';
    
                        testResults.push({testName: 'success-feedwall', testOutput: successFeedWallOutput});
                    }
                    else {
                        successFeedWallOutput = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'success-feedwall', testOutput: successFeedWallOutput});
                    }
                },
                function () {
                    successFeedWallOutput = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'success-feedwall', testOutput: successFeedWallOutput});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();
          });
}

function startExistingFeedWall() {
    let ret = false;
    driver.findElement(By.name('info-save')).clear();
    driver.findElement(By.name('title-save')).clear();
    driver.findElement(By.name('title-save')).sendKeys('new title');
    driver.findElement(By.name('info-save')).sendKeys('new info');
    
    driver.sleep(500).then(function () {
          driver.findElement(By.name('save')).click();
    });
    
    var existingFeedWallOutput = '';
    
    driver.sleep(500).then(
        function() {
            driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                  function (text) {
                    if (text === 'קיים עדכון עם תוכן זהה') {
                        existingFeedWallOutput = 'passed';
    
                        testResults.push({testName: 'existing-feedwall', testOutput: existingFeedWallOutput});

                        ret = true;
                    }
                    else {
                        existingFeedWallOutput = 'failed - alert shown was incorrect: ' + text;
    
                        testResults.push({testName: 'existing-feedwall', testOutput: existingFeedWallOutput});
                    }
                },
                function () {
                    existingFeedWallOutput = 'failed - no alert was shown.';
    
                    testResults.push({testName: 'existing-feedwall', testOutput: existingFeedWallOutput});
                });
    
            driver.findElement(By.className('md-confirm-button')).click();

            return ret;
          });
}

function startDeleteFeedWall() {
    driver.findElement(By.name('delete-last')).sendKeys(webdriver.Key.ENTER);

    let deleteFeedWallOutput = '';
    driver.sleep(500).then(
        function() {
            driver.findElement(By.className('md-focused')).click();

            driver.sleep(4500).then(
                function() {
                    driver.findElement(By.xpath("//p[@class='ng-binding']")).getText().then(
                        function (text) {
                            if (text === 'העדכון נמחק בהצלחה') {
                                deleteFeedWallOutput = 'passed';
            
                                testResults.push({testName: 'delete-feedwall', testOutput: deleteFeedWallOutput});
                            }
                            else {
                                deleteFeedWallOutput = 'failed - alert shown was incorrect: ' + text;
            
                                testResults.push({testName: 'delete-feedwall', testOutput: deleteFeedWallOutput});
                            }
                        },
                        function () {
                            deleteFeedWallOutput = 'failed - no alert was shown.';
            
                            testResults.push({testName: 'delete-feedwall', testOutput: deleteFeedWallOutput});
                        });
    
                    driver.findElement(By.className('md-confirm-button')).click();
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
