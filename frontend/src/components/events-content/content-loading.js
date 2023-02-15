import EventItemLoading from './item-loading';
import Skeleton from '@mui/material/Skeleton';

const ContentLoading = () => {
    return (
        <div className="event-page">
            <div className="event-filter">
                <Skeleton width="140px" height="60px"/>
                <Skeleton sx={{marginTop: "10px"}} height="80px"/>
                <Skeleton width="50%" height="40px"/>
                {[...Array(7)].map((e, i) => <Skeleton height="40px" key={i}/>)}
                <Skeleton width="50%" height="40px"/>
                {[...Array(10)].map((e, i) => <Skeleton height="40px" key={7 + i}/>)}
            </div>

            <div style={{width: "100%"}}>
                <div className="event-content-intro">
                    <Skeleton width="140px" height="60px"/>
                    <div className="flex-row">
                        <Skeleton width="140px" height="60px"/>
                        <Skeleton width="140px" height="60px"/>
                    </div>
                </div>
                <section className="event-list">
                    {[...Array(12)].map((e, i) => <EventItemLoading key={17 + i}/>)}
                </section>
            </div>
        </div>
    );
};

export default ContentLoading