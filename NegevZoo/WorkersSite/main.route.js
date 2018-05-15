app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('login', {
            url:            '/login',
            templateUrl:    'login/login.html',
            controller:     'loginCtrl'
        })
        .state('mainMenu', {
            url:            '/mainMenu',
            templateUrl:    'mainMenu/mainMenu.html',
            controller:     'zooMainMenuCtrl'
        })
        .state('mainMenu.feedWall', {
            url:            '/feedWall',
            templateUrl:    '/mainMenu/feedWall/feedWall.html',
            controller:     'zooFeedWallCtrl'
        })
        .state('mainMenu.enclosures', {
            url:            '/enclosures',
            template:       '<ui-view></ui-view>',
            abstract:       true
        })
        .state('mainMenu.enclosures.list', {
            url:            '/list',
            templateUrl:    'mainMenu/enclosuresList/enclosuresList.html',
            controller:     'zooEnclosuresListCtrl'
        })
        .state('mainMenu.enclosures.enclosure', {
            url:            '/enclosure',
            templateUrl:    'mainMenu/enclosure/enclosure.html',
            controller:     'zooEnclosureCtrl',
            params: {
                enclosure:      undefined,
            }
        })
        .state('mainMenu.enclosures.animal', {
            url:            '/animal',
            templateUrl:    'mainMenu/animal/animal.html',
            controller:     'zooAnimalCtrl',
            params: {
                animal:       undefined,
                enclosure:    undefined
            }
        })
        .state('mainMenu.enclosures.animalStory', {
            url:            '/animalStory',
            templateUrl:    'mainMenu/animalStory/animalStory.html',
            controller:     'zooAnimalStoryCtrl',
            params: {
                animalStory:  undefined,
                enclosure:    undefined
            }
        })
        .state('mainMenu.specialEvents', {
            url:            '/specialEvents',
            templateUrl:    '/mainMenu/specialEvents/specialEvents.html',
            controller:     'zooSpecialEventsCtrl'
        })
        .state('mainMenu.map', {
            url:            '/map',
            templateUrl:    '/mainMenu/map/map.html',
            controller:     'zooMapCtrl'
        })
        .state('mainMenu.userControl', {
            url:            '/userControl',
            templateUrl:    '/mainMenu/userControl/userControl.html',
            controller:     'zooUserControlCtrl'
        })
        .state('mainMenu.generalInfo', {
            url:            '/generalInfo',
            templateUrl:    '/mainMenu/generalInfo/generalInfo.html',
            controller:     'zooGeneralInfoCtrl',
        })
        .state('mainMenu.generalInfo.contactInfo', {
            url:            '/contactInfo',
            templateUrl:    '/mainMenu/generalInfo/contactInfo/contactInfo.html',
            controller:     'zooContactInfoCtrl',
        })
        .state('mainMenu.generalInfo.prices', {
            url:            '/prices',
            templateUrl:    '/mainMenu/generalInfo/prices/prices.html',
            controller:     'zooPricesCtrl',
        })
        .state('mainMenu.generalInfo.openingHours', {
            url:            '/openingHours',
            templateUrl:    '/mainMenu/generalInfo/openingHours/openingHours.html',
            controller:     'zooOpeningHoursCtrl',
        })
        .state('mainMenu.generalInfo.aboutUs', {
            url:            '/aboutUs',
            templateUrl:    '/mainMenu/generalInfo/aboutUs/aboutUs.html',
            controller:     'zooAboutUsCtrl',
        });
});