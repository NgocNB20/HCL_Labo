function createDate(day, month, year) {
    for(var i = 1; i < 32; i++) {
        let option = "";
        if(i === parseInt(day)) {
            option = "<option value=" + i + " selected='true'>" + i + "</option>";
        } else {
            option = "<option value=" + i + ">" + i + "</option>";
        }
        $("#select-birth-day select").append(option);
    }

    for(var i = 1; i < 13; i++) {
        let option = "";
        if(i === parseInt(month)) {
            option = "<option value=" + i + " selected='true'>" + i + "</option>";
        } else {
            option = "<option value=" + i + ">" + i + "</option>";
        }
        $("#select-birth-month select").append(option);
    }

    let curYear = new Date().getFullYear();
    for(var i = 0 ; i < 101; i++) {
        let option = "";
        if((curYear - i) === parseInt(year)) {
            option = "<option value=" + (curYear - i) + " selected='true'>" + (curYear - i) + "</option>";
        } else {
            option = "<option value=" + (curYear - i) + ">" + (curYear - i) + "</option>";
        }
        $("#select-birth-year select").append(option);
    }
}
