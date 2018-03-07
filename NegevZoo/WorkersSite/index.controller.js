app.controller('indexController', 
	['$scope', 
     '$state', 
     'enclosureService',
	 
	    function indexController($scope, $state, enclosureService) 
        {
            allEnclosuresQuery = enclosureService.getAllEnclosures(1);

            $state.go('login');

}]);