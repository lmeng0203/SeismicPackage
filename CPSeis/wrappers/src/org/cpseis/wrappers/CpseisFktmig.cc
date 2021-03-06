//////// DO NOT EDIT THIS FILE - it is machine generated ////////

#include "CpseisFktmig.hh"

//------------------ fortran spelling adjustments --------------------//
//------------------ fortran spelling adjustments --------------------//
//------------------ fortran spelling adjustments --------------------//

#if NEED_UNDERSCORE
#define fktmig_wrap_create             fktmig_wrap_create_
#define fktmig_wrap_delete             fktmig_wrap_delete_
#define fktmig_wrap_update             fktmig_wrap_update_
#define fktmig_wrap_wrapup             fktmig_wrap_wrapup_
#define fktmig_wrap_oneset             fktmig_wrap_oneset_
#define fktmig_wrap_twosets            fktmig_wrap_twosets_
#elif NEED_CAPITALS
#define fktmig_wrap_create             FKTMIG_WRAP_CREATE
#define fktmig_wrap_delete             FKTMIG_WRAP_DELETE
#define fktmig_wrap_update             FKTMIG_WRAP_UPDATE
#define fktmig_wrap_wrapup             FKTMIG_WRAP_WRAPUP
#define fktmig_wrap_oneset             FKTMIG_WRAP_ONESET
#define fktmig_wrap_twosets            FKTMIG_WRAP_TWOSETS
#endif

//----------------------- fortran prototypes -------------------------//
//----------------------- fortran prototypes -------------------------//
//----------------------- fortran prototypes -------------------------//

extern "C"
    {
    CpseisBase::ModuleCreate   fktmig_wrap_create;
    CpseisBase::ModuleDestroy  fktmig_wrap_delete;
    CpseisBase::ModuleUpdate   fktmig_wrap_update;
    CpseisBase::ModuleWrapup   fktmig_wrap_wrapup;
    CpseisBase::ModuleOneset   fktmig_wrap_oneset;
    CpseisBase::ModuleTwosets  fktmig_wrap_twosets;
    }

//------------------------ constructor -------------------------------//
//------------------------ constructor -------------------------------//
//------------------------ constructor -------------------------------//

CpseisFktmig::CpseisFktmig() : CpseisBase ("FKTMIG",
                                       fktmig_wrap_create,
                                       fktmig_wrap_delete,
                                       fktmig_wrap_update,
                                       fktmig_wrap_wrapup,
                                       fktmig_wrap_oneset,
                                       fktmig_wrap_twosets) {}

//------------------------------ end ---------------------------------//
//------------------------------ end ---------------------------------//
//------------------------------ end ---------------------------------//
