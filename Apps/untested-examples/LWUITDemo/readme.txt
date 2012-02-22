Unlike the older LWUIT demo the new LWUIT demo follows the new recommended project structure from the LWUIT team.

The new structure contains all of the portable LWUIT code in a single library project and contains platform specific
code as sub projects of this parent project.
This allows us to avoid the use of a preprocessor and build to multiple target types far more easily.

This structure also allows developers to better exploit platform specific features when necessary by placing such features
within the platform project rather than placing them in the parent LWUIT project.