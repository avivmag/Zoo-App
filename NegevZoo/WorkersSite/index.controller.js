app.controller('indexController', 
	['$scope', 
	 '$state', 
	 
	    function indexController($scope, $state) 
	    {
	        $state.go('login');
}]);