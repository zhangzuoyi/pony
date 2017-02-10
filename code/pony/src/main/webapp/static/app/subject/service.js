'use strict';

App.factory('SubjectService', ['$http', '$q',"context", function($http, $q, context){

	return {
		
		findAll: function() {
					return $http.get(context+'/subject/list')
							.then(
									function(response){
										return response.data;
									}, 
									function(errResponse){
										console.error('Error while fetching users');
										return $q.reject(errResponse);
									}
							);
			},
		findClassSubject: function() {
			return $http.get(context+'/subject/findClassSubject')
					.then(
							function(response){
								return response.data;
							}, 
							function(errResponse){
								console.error('Error while fetching users');
								return $q.reject(errResponse);
							}
					);
			}
		
	};
}]);
