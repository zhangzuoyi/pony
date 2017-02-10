'use strict';

App.factory('LessonArrangeService', ['$http', '$q', "context", function($http, $q, context){

	return {
		
		findByClass: function(classId) {
					return $http.get(context+'/lessonArrange/findByClass',{params:{classId:classId}})
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
		addArrange: function(la) {
			return $http.post(context+'/lessonArrange/add',la)
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
		editArrange: function(la) {
			return $http.post(context+'/lessonArrange/edit',la)
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
		deleteArrange: function(arrangeId) {
			return $http.post(context+'/lessonArrange/delete/'+arrangeId)
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
