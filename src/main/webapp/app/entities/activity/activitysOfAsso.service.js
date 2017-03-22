(function() {
    'use strict';
    angular
        .module('amasApp')
        .factory('ActivitysOfAsso', ActivitysOfAsso);

    ActivitysOfAsso.$inject = ['$resource', 'DateUtils'];

    function ActivitysOfAsso ($resource, DateUtils) {
        var resourceUrl =  'api/activitiesOfAsso/:id';

        return $resource(resourceUrl, {id:'@assoId'}, {
            'queryActivitysOfAsso': { method: 'GET', isArray: true}
        });
    }
})();
