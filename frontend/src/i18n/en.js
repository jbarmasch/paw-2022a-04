const i18n_en = {
    app: "Botpass",
    submit: "Submit",
    fieldRequired: "This field is required",
    fieldInvalid: "This field is invalid",
    loading: "Loading...",
    autocompleteNoOptions: "No options",
    filters: "Filters",
    noData: "No data",
    seeEvents: "See events",
    landing: "Landing image",

    error: {
        api: "¡Uh oh! An unexcpected error has occured."
    },

    order : {
        dateAsc : "Ascending date",
        dateDesc: "Descending date",
        ratingAsc: "Ascending rating",
        ratingDesc: "Descending rating",
        usernameAsc: "Ascending username",
        usernameDesc: "Descending username",
    },

    nav: {
        events: "Events",
        createEvent: "Create event",
        bookings: "Bookings",
        myEvents: "My events",
        account: "Account",
        organizers: "Organizers",
        profile: "Profile",
        logout: "Logout",
        searchEvent: "Search events"
    },

    home: {
        phrase: "Come have fun with us!",
        page: "Home",
        events: "Events",
        next: "Next",
        previous: "Previous",
        recommended: "Recommended events just for you",
        trending: "Trending concerts right now",
        featured: "See featured events",
        createTitle: "Create your event for free",
        createDesc: "Use BotPass to have an extended reach",
        rangeTitle: "BotPass offers a huge range of events",
        rangeDesc: "From sport events to parties, BotPass offers it all",
        bookTitle: "Book from the comfort of your home",
        bookDesc: "Our easy booking system lets you book instantly",
    },

    create: {
        name: "Event name",
        description: "Description",
        location: "Select location",
        type: "Select event type",
        tags: "Select tags",
        hasMin: "Min. age",
        minAge: "Min. age",
        uploadFile: 'Upload event picture',
        date: 'Event date',
        dateError: "The event must not have ended",
        imageError: "The image size must be less than 1MB",
        maxLengthDescription: "Description length must be up to 1000 characters",
        maxLengthName: "Name length must be up to 100 characters",
    },

    myEvents: {
        title: "My events",
        ticketDateError: "Starting date must be before until date",
        ticketPriceError: "Ticket price should be at least 0",
        ticketQtyError: "There should be at least 1 ticket",
        ticketsPerUserError: "The amount of tickets per user should be between 1 & 10",
        ticketsLeftError: "Te quedaste sin tickets hermano"
    },

    event: {
        name: "Event name",
        description: "Event description",
        minPrice: "Starting price",
        type: "Event type",
        location: "Location",
        tags: "Tags",
        ticket: "Ticket name",
        price: "Price",
        quantity: "Quantity",
        date: "Date",
        maxPUser: "Max. per user",
        sureDelete: "Are you sure you want to delete this event?",
        until: "Until",
        free: "Free",
        noTickets: "No tickets",
        selectQty: "Select quantity",
        starting: "Starting at ",
        similarPl: "Similar events",
        similarSi: "Here's a similar event",
        recommendedPl: "Recommended events",
        recommendedSi: "Here's a recommended event",
        upcomingPl: "Don't miss out on the next events",
        upcomingSi: "Don't miss out on the next event",
        fewTicketsPl: "These events are running out of tickets",
        fewTicketsSi: "This event is running out of tickets",
        book: "Book",
        soldOut: "Sold out",
        minAge: "Min. age",
        minAgeText: "From ",
        organizer: "Organizer",
        noEvents: "No events found",
        event: "Event",
        over: "Over",
        bookingError: "The amount of tickets selected must be greater than 0 (zero)",
        enable: "Enable",
        disable: "Disable",
    },

    login: {
        login: "Log in",
        username: "Username",
        password: "Password",
        keepMe: "Keep me signed in",
        forgot: "Forgot your password?",
        signIn: "Sign in",
        notAMember: "Not a member yet?",
        signUp: "Sign up",
        notFound: "Username or password is incorrect"
    },

    filter: {
        title: "Events",
        locations: "Select locations",
        types: "Select types",
        tags: "Select tags",
        sortBy: "Sort by",
        order: "Order",
        price: "Price",
        apply: "Apply",
        minPrice: "Min. price",
        maxPrice: "Max. price",
        ascending: "Ascending",
        descending: "Descending",
        username: "Username",
        rating: "Rating",
        soldOut: "Sold out",
        noTickets: "Ticketless",
        advancedOptions: "Advanced options",
        minPriceError: "Minimum price must be greater or equal than 0",
        maxPriceError: "Maximum price must be greater or equal than 0",
        rangePriceError: "Maximum price cannot be lower than minimum price",
        clear: "Clear filters"
    },

    bookings: {
        title: "Bookings",
        booking: "Booking",
        all: "All bookings",
        cancel: "Cancel",
        ticket: "Ticket",
        qty: "Quantity",
        price: "Price",
        rate: "Rate",
        cancelMessage: "Are you sure you want to cancel your booking?",
        accept: "Accept",
        confirm: "Confirm",
        invalidate: "Invalidate",
        noBookings: "No bookings found",
    },

    stats: {
        stats: "Stats",
        eventsAttended: "Booked events",
        ticketsBooked: "Tickets booked",
        favType: "Favorite event type",
        favLocation: "Favorite location"
    },

    register: {
        register: "Register",
        create: "Create an account to create events and book tickets",
        mail: "Email",
        username: "Username",
        pass: "Password",
        repeat: "Repeat password",
        mailPatternError : "Invalid mail",
        passwordPatternError : "Password can only contain lowercase and uppercase letters and digits",
        passwordLenError : "Password must be between 8 and 100 characters long",
        passwordMatchError : "Passwords don't match",
        usernameLenError : "Username must be between 8 and 100 characters long",
        notFoundError : "Incorrect username or password",
    },

    footer: {
        text: "BotPass connects event organizers with people, giving visibility to less known events as well as providing a platform for organizers to sell tickets and visualize stats.",
        contact: "Contact",
    },

    organizer: {
        organizers: "Organizers",
        seeEvents: "See events",
        noEvents: "No events",
        searchOrganizer: "Search organizer",
        noOrganizers: "No organizers found"
    },

    thankYou: {
        phrase: "Thank you for booking with BotPass"
    },

    eventStats: {
        stats: "Event stats",
        attended: "Attended",
        booked: "Amount of people that booked",
        attendance: "Attendance percentage",
        saleRatio: "Sale ratio",
        income: "Income",
        expected: "Expected income"
    },

    ticketStats: {
        ticketName: "Ticket name",
        attendance: "Attendance",
        saleRatio: "Sale ratio",
        price: "Price",
        realQty: "Real quantity",
        qty: "Quantity",
        income: "Income",
        booked: "Booked",
    }
};

export default i18n_en;
