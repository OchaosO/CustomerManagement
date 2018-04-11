/**
 * 
 */
var serviceURI = "/CustomerManagementWeb/api/v1/customers/list";
//Model anlegen
var cmApp = angular.module("cmApp",[]);

cmApp.controller("cmStartViewController", ['$scope','$http',function($scope,$http){
	
	$scope.customers = [];
	$scope.searchFilter = '';
	$scope.search = '';
	
	
	$scope.getAllCustomers = function () {
	$http({
		method: 'GET',
		url: serviceURI
	}).then(function sucessCallback(response) {
		$scope.customers = response.data;
	}, function errorCallback(response) {
		console.log("Error");
	})};
	

	$scope.applySearchFilter = function() {
		$scope.searchFilter = $scope.search;
	}
	
	$scope.getRealtionship = function(relationship) {
		if (relationship == 'Friend') {
			return "Freund";
		}
		if (relationship == 'Colleague') {
			return "Kollege";
		}
		if (relationship == 'Job') {
			return "Arbeitsbeziehung";
		}
		if (relationship == 'Family') {
			return "Familie";
		}
		return "Unbekannt";
	}
	
	$scope.getAllCustomers($scope,$http);
	
	
}]);