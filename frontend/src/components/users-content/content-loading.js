import UserLoading from './user-loading';
import Skeleton from '@mui/material/Skeleton';


const ContentLoading = () => {
    return (
        <section className="users-content products-content">
            <div className="bookings-intro">
                <Skeleton width="150px" height="60px"/>
                <div className="users-filter">
                    <Skeleton width="150px" height="60px"/>
                    <Skeleton width="150px" height="60px"/>
                </div>
                {/* <UserLoading/>
                <UserLoading/>
                <UserLoading/>
                <UserLoading/>
                <UserLoading/>
    <UserLoading/> */}
            </div>
            <div className="user-list">
                {[...Array(12)].map((e, i) => <UserLoading key={i} />)}
            </div>
        </section>
    );
};

export default ContentLoading