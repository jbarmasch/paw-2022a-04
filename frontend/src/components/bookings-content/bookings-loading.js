import BookingItemLoading from './item-loading';
import Skeleton from '@mui/material/Skeleton';


const BookingLoading = () => {
    return (
        <section className="users-content products-content">
            <div className="products-content__intro bookings-intro">
                <Skeleton width="170px" height="60px" />
                <Skeleton width="170px" height="60px" />
            </div>

            <div>
                {[...Array(8)].map((e, i) => <BookingItemLoading key={i} />)}
            </div>
            {/*<BookingItemLoading/>
            <BookingItemLoading/>
            <BookingItemLoading/>
            <BookingItemLoading/>
            <BookingItemLoading/>
            <BookingItemLoading/>*/}
        </section>
    );
};


{/* 

        <section className="users-content products-content">
            <div className="products-content__intro">
            <h2>{i18n.t("bookings.title")}</h2>
            <button type="button" onClick={() => setOrderProductsOpen(!orderProductsOpen)}
                    className="products-filter-btn"><i className="icon-filters"></i></button>
                <form className={`products-content__filter ${orderProductsOpen ? 'products-order-open' : ''}`}>
                        <div className="products__filter__select">
                            <Controller
                                control={control}
                                defaultValue={'DATE_ASC'}
                                name="order"
                                render={({ field: { onChange, value, name, ref } }) => {
                                    const currentSelection = orderList.find(
                                        (c) => c.value === value
                                    );

                                    const handleSelectChange = (selectedOption) => {
                                        onChange(selectedOption.target.value);
                                        setOrder(selectedOption.target.value);
                                    };

                                    return (
                                        <FormControl>
                                            <InputLabel className="order-label" id="order-select-label">{i18n.t("filter.sortBy")}</InputLabel>
                                            <Select
                                                className="order-select"
                                                labelId="order-select-label"
                                                id="order-select"
                                                value={order ? order : "DATE_ASC"}
                                                onChange={handleSelectChange}
                                                input={<OutlinedInput className="order-label" label={i18n.t("filter.sortBy")} />}
                                            >
                                                {orderList.map((x) => (
                                                    <MenuItem
                                                        key={x.value}
                                                        value={x.value}
                                                    >
                                                        {x.label}
                                                    </MenuItem>
                                                ))}
                                            </Select>
                                        </FormControl>
                                    );
                                }}
                            />
                        </div>
                    </form>
        </div>
        <div>
            <div>
                {data && <Page data={data} aux={child} setAux={setChild} mutate={mutate} userId={userId} />}
            </div>
            <div className="pagination">
                <Pagination count={Number(links.last.page)} showFirstButton showLastButton page={pageIndex} onChange={handlePageChange} />
            </div>
        </div>
    </section>

*/}

export default BookingLoading