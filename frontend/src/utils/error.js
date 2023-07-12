import i18n from "../i18n";

export const getErrorMessage = (args) => {
    try {
        let error = JSON.parse(args)
        let message = error["message"]
        if (message) {
            return message
        } else {
            return i18n.t("error.api")
        }
    } catch (err) {
        return i18n.t("error.api")
    }
}

export const getErrorsParsed = (args) => {
    try {
        let errors = JSON.parse(args)
        return errors
    } catch (err) {
        return null
    }
}