'use strict';

describe('Controller Tests', function() {

    describe('Advertisement Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAdvertisement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAdvertisement = jasmine.createSpy('MockAdvertisement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Advertisement': MockAdvertisement
            };
            createController = function() {
                $injector.get('$controller')("AdvertisementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'dniprolabApp:advertisementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
