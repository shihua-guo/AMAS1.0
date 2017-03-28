/*
* @Author: guosh3
* @Date:   2017-03-27 15:30:44
* @Last Modified by:   shihua
* @Last Modified time: 2017-03-28 17:36:45
*/
'use strict';
(function () {
	// body...
	angular
	.module('amasApp')
	.controller('amemberChartsController', amemberChartsController);
	amemberChartsController.$inject = ['$scope', '$http'];
	function amemberChartsController($scope,$http) {
		$scope.selectAsso = {
			allAssoIdAndName:[{id:"全部",assoName:"全部社团"}],
			selected:{id:"全部",assoName:"全部社团"},
			selected2:{id:"全部",assoName:"全部社团"},
			selected3:{id:"全部",assoName:"全部社团"},
			selected4:{id:"全部",assoName:"全部社团"}
		}
		//获取社团的名称
		$http.get('/api/getAssoIdAndName').success(function(data, status) {
			for (var i in data) {
				$scope.selectAsso.allAssoIdAndName.push(data[i]);
			}
		});
	};

}
)();