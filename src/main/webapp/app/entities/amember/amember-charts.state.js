(function() {
    'use strict';

    angular
        .module('amasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('asso-chart', {
            parent: 'app',
            url: '/asso-chart',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.amember.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/amember/assoCharts.html',
                    controller: 'amemberChartsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('amember');
                    return $translate.refresh();
                }]
            }
        })
        .state('amem-chart', {
            parent: 'app',
            url: '/amember-chart',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.amember.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/amember/amemCharts.html',
                    controller: 'amemberChartsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('amember');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
