function hasMinimumAge() {
    var checkBox = document.getElementById("hasMinAge");
    var minAgeInput = document.getElementById("minAgeInput")

    minAgeInput.disabled = !checkBox.checked;
}

(function () {
    var name = document.getElementById('name');
    var description = document.getElementById('description');
    var date = document.getElementById('date');
    var age = document.getElementById('minAgeInput');
    var type = document.getElementById('type');
    var location = document.getElementById('location');
    var form = document.getElementById('eventForm');

    var checkNameValidity = function () {
        if (name.validity.tooLong) {
            name.setCustomValidity('${nameSizeError}');
            updateNameMessage()
        } else if (name.validity.valueMissing) {
            name.setCustomValidity('${nameEmptyError}');
            updateNameMessage()
        } else {
            name.setCustomValidity('');
        }
    };

    var checkTypeValidity = function () {
        if (type.validity.valueMissing) {
            type.setCustomValidity('${typeEmptyError}');
            updateTypeMessage()
        } else {
            type.setCustomValidity('');
        }
    };

    var checkLocationValidity = function () {
        if (location.validity.valueMissing) {
            location.setCustomValidity('${locationEmptyError}');
            updateLocationMessage()
        } else {
            location.setCustomValidity('');
        }
    };

    var checkAgeValidity = function () {
        if (age.validity.valueMissing) {
            age.setCustomValidity('${minAgeNullError}');
            updateAgeMessage()
        } else if (parseInt(age.value) < 14 || parseInt(age.value) > 27) {
            age.setCustomValidity('${rangeAgeMinError}');
            updateAgeMessage();
        } else {
            age.setCustomValidity('');
        }
    };

    var checkDescriptionValidity = function () {
        if (description.validity.tooLong) {
            description.setCustomValidity('${descriptionSizeError}');
            updateDescriptionMessage();
        } else {
            description.setCustomValidity('');
        }
    };

    var checkDateValidity = function () {
        if (date.validity.valueMissing) {
            date.setCustomValidity('emilia');
            updateDateMessage();
        } else if (date.validity.typeMismatch) {
            date.setCustomValidity('cami');
            updateDateMessage();
        } else if (date.validity.rangeUnderflow) {
            date.setCustomValidity('rodrigo');
            updateDateMessage();
        } else {
            date.setCustomValidity('');
        }
    };

    var updateNameMessage = function () {
        form.getElementsByClassName('formError')[0].innerHTML = name.validationMessage;
    }

    var updateDescriptionMessage = function () {
        form.getElementsByClassName('formError')[1].innerHTML = description.validationMessage;
    }

    var updateLocationMessage = function () {
        form.getElementsByClassName('formError')[2].innerHTML = location.validationMessage;
    }

    var updateTypeMessage = function () {
        form.getElementsByClassName('formError')[3].innerHTML = type.validationMessage;
    }

    var updateDateMessage = function () {
        form.getElementsByClassName('formError')[4].innerHTML = date.validationMessage;
    }

    var updateAgeMessage = function () {
        form.getElementsByClassName('formError')[7].innerHTML = age.validationMessage;
    }

    name.addEventListener('change', checkNameValidity, false);
    name.addEventListener('keyup', checkNameValidity, false);
    age.addEventListener('change', checkAgeValidity, false);
    age.addEventListener('keyup', checkAgeValidity, false);
    description.addEventListener('change', checkDescriptionValidity, false);
    description.addEventListener('keyup', checkDescriptionValidity, false);
    location.addEventListener('change', checkLocationValidity, false);
    location.addEventListener('keyup', checkLocationValidity, false);
    type.addEventListener('change', checkTypeValidity, false);
    type.addEventListener('keyup', checkTypeValidity, false);
    date.addEventListener('change', checkDateValidity, false);
    date.addEventListener('keyup', checkDateValidity, false);


    form.addEventListener('submit', function (event) {
        if (form.classList) form.classList.add('submitted');
        checkNameValidity()
        checkDescriptionValidity();
        checkLocationValidity();
        checkTypeValidity();
        checkDateValidity();
        checkAgeValidity();
        if (!this.checkValidity()) {
            event.preventDefault();
            updateNameMessage();
            updateDescriptionMessage();
            updateLocationMessage();
            updateTypeMessage();
            updateDateMessage();
            updateAgeMessage();
        }
    }, false);
}());
