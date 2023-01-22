import {useIntl} from "react-intl";


export const getPrice = (price) => {
    const intl = useIntl()
    if (price > 0)
        return "$" + price;
    if (price == 0)
        return intl.formatMessage({id: "event.price"})
    return intl.formatMessage({id: "event.noTickets"})
}