'use strict';

App.factory('StudentService', ['$http', '$q',"context", function($http, $q, context){

	return {
		
		mycourse: function() {
					return $http.get(context+'/student/mycourse')
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
