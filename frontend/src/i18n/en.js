const i18n_en = {
    app: "Bottler",
    submit : "Submit",
    fieldRequired : "This field is required",
    fieldInvalid : "This field is invalid",
    passwordMatch : "Passwords do not match",
    passwordInvalid : "Password is invalid",
    loading : "Loading...",
    autocompleteNoOptions: "No options",
    filters: "Filters",
    noData : "No data",
    
    nav: {
        events : "Events",
        createEvent : "Create event",
        bookings : "Bookings",
        myEvents : "My events",
        account : "Account",
        organizers : "Organizers",
        profile : "Profile",
        logout : "Logout"
    },

    home : {
        phrase : "Come have fun with us!",
        page : "Home",
        events : "Events",
        next : "Next",
        previous : "Previous",
        recommended : "Recommended events just for you",
        trending : "Trending concerts right now",
        featured : "See featured events",
    },

    create: {
        name : "Event name",
        description : "Description",
        location : "Select location",
        type : "Select event type",
        tags: "Select tags",
        hasMin : "Min. age",
        selectMinAge : "Select min. age",
        minAge : "Min. age",
        uploadFile : 'Upload event picture',
        date : 'Event date',
        dateError : "The event must not have ended"
    },

    event: {
        name : "Event name",
        description : "Event description",
        minPrice : "Starting price",
        type : "Event type",
        location : "Location",
        tags : "Tags",
        price : "Price",
        quantity : "Quantity",
        maxpuser : "Max. per user",
        starting : "Starting at",
        until : "Until",
        free : "Free",
        noTickets : "No tickets",
        selectQty : "Select quantity",
        starting : "Starting at ",
        similarPl : "Similar events",
        similarSi : "Here's a similar event",
        recommendedPl : "Recommended events",
        recommendedSi : "Here's a recommended event",
        book : "Book",
        soldOut: "Sold out",
        minAge: "Min. age",
        minAgeText : "From ",
        organizer : "Organizer"
        
    },

    login: {
        login : "Log in",
        username : "Username",
        password : "Password",
        keepMe : "Keep me signed in",
        forgot : "Forgot your password?",
        signIn : "Sign in",
        notAMember : "Not a member yet?",
        signUp : "Sign up"
    },

    filter: {
        title : "Events",
        locations : "Select locations",
        types : "Select types",
        tags : "Select tags",
        sortBy : "Sort by",
        order : "Order",
        price : "Precio",
        apply : "Aplicar",
        minPrice : "Precio mín.",
        maxPrice : "Precio máx.",
        ascending : "Ascending",
        descending : "Descending",
        username : "Username",
        rating : "Rating",
        soldOut : "Sold out",
        noTickets : "Ticketless",
        advancedOptions: "Advanced options",
        minPriceError : "Minimum price must be greater or equal than 0",
        maxPriceError : "Maximum price must be greater or equal than 0",
        rangePriceError : "Maximum price cannot be lower than minimum price"
    },   

    bookings: {
        title : "Bookings",
        booking : "Booking",
        all : "All bookings",
        cancel : "Cancel",
        ticket : "Ticket",
        qty : "Quantity",
        price : "Price",
        rate : "Rate",
        cancelMessage : "Are you sure you want to cancel your booking?",
        accept : "Accept"
    },

    stats: {
        stats : "Stats",
        eventsAttended : "Booked events",
        ticketsBooked : "Tickets booked",
        favType : "Favorite event type",
        favLocation : "Favorite location"
    },

    myEvents: {
        title : "My events",
        ticketDateError : "STARTING > UNTIL GILASTRUM",
        ticketPriceError : "Ticket price should be at least 0",
        ticketQtyError : "There should be at least 1 ticket",
        ticketsPerUserError : "The amount of tickets per user should be between 1 & 10",
        ticketsLeftError : "Te quedaste sin tickets hermano"
    },

    register: {
        create : "Create an account to create events and book tickets",
        name : "Username",
        pass : "Password",
        repeat : "Repeat password"
    },

    footer: {
        text: "BotPass connects event organizers with people, giving visibility to less known events as well as providing a platform for organizers to sell tickets and visualize stats.",
        events: "Events",
        recommended: "Recommended",
        popular: "Popular",
        contact: "Contact",
    },

    organizer: {
        organizers : "Organizers",
        seeEvents : "See events",
        noEvents : "No events"
    },

    thankYou: {
        phrase : "Thank you for booking with BotPass"
    }
};

export default i18n_en;
