export const formatPrice = (price) => {
    if (!price) return;

    return price.toLocaleString('en-US')
}

export const convertPrice = (string) => {
    return parseFloat(string.replace(/,/g, ''))
}